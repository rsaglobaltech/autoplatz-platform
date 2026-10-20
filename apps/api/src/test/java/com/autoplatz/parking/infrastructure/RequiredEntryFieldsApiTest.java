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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.web.servlet.MockMvc;

/** The API end to end: an entry missing a required field is refused with the missing fields, and nothing is stored. */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class RequiredEntryFieldsApiTest {
  @Autowired MockMvc mvc;
  @Autowired JdbcClient jdbc;

  @Test
  @DisplayName("REQ-015 SCN-025: an empty license plate with the type \"Car\" is refused and the plate is reported missing")
  void aMissingLicensePlateBlocksTheSave() throws Exception {
    long before = storedEntries();
    String body = "{\"licensePlate\":\"\",\"vehicleTypeId\":\"" + idOf("Car") + "\"}";

    mvc.perform(post("/api/entries").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isUnprocessableContent())
        .andExpect(jsonPath("$.code").value("missing_required_fields"))
        .andExpect(jsonPath("$.fields", contains("licensePlate")));

    assertThat(storedEntries()).isEqualTo(before);
  }

  @Test
  @DisplayName("REQ-015 SCN-026: the plate \"AB123CD\" with no vehicle type is refused and the type is reported missing")
  void aMissingVehicleTypeBlocksTheSave() throws Exception {
    long before = storedEntries();

    for (String body : List.of("{\"licensePlate\":\"AB123CD\",\"vehicleTypeId\":\"\"}",
        "{\"licensePlate\":\"AB123CD\",\"vehicleTypeId\":null}", "{\"licensePlate\":\"AB123CD\"}")) {
      mvc.perform(post("/api/entries").contentType(MediaType.APPLICATION_JSON).content(body))
          .andExpect(status().isUnprocessableContent())
          .andExpect(jsonPath("$.code").value("missing_required_fields"))
          .andExpect(jsonPath("$.fields", contains("vehicleType")));
    }

    assertThat(storedEntries()).isEqualTo(before);
  }

  private long storedEntries() {
    return jdbc.sql("SELECT count(*) FROM parking_entry").query(Long.class).single();
  }

  private String idOf(String name) throws Exception {
    String json = mvc.perform(get("/api/vehicle-types")).andReturn().getResponse().getContentAsString();
    List<String> ids = JsonPath.read(json, "$[?(@.name == '" + name + "')].id");
    return ids.getFirst();
  }
}
