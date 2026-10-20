package com.autoplatz.parking.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.autoplatz.TestcontainersConfiguration;
import com.jayway.jsonpath.JsonPath;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

/**
 * The API end to end: an entry saved through the form is stored in PostgreSQL with its plate normalized.
 * It adds entries, so the context (and its PostgreSQL) is not shared with the tests after it.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@DirtiesContext
class RegisterEntryApiTest {
  @Autowired MockMvc mvc;
  @Autowired JdbcClient jdbc;

  @Test
  @DisplayName("REQ-014 SCN-024: a plate typed \"ab 123 cd\" is stored as \"AB123CD\"")
  void aLowercasePlateWithSpacesIsStoredNormalized() throws Exception {
    String car = idOf("Car");
    String body = "{\"licensePlate\":\"ab 123 cd\",\"vehicleTypeId\":\"" + car + "\"}";

    String id = JsonPath.read(
        mvc.perform(post("/api/entries").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.licensePlate").value("AB123CD"))
            .andExpect(jsonPath("$.vehicleTypeId").value(car))
            .andReturn().getResponse().getContentAsString(), "$.id");

    String stored = jdbc.sql("SELECT license_plate FROM parking_entry WHERE id = :id")
        .param("id", UUID.fromString(id)).query(String.class).single();
    assertThat(stored).isEqualTo("AB123CD");
    mvc.perform(get("/api/entries")).andExpect(status().isOk())
        .andExpect(jsonPath("$[?(@.id == '" + id + "')].licensePlate", contains("AB123CD")));
  }

  private String idOf(String name) throws Exception {
    String json = mvc.perform(get("/api/vehicle-types")).andReturn().getResponse().getContentAsString();
    List<String> ids = JsonPath.read(json, "$[?(@.name == '" + name + "')].id");
    return ids.getFirst();
  }
}
