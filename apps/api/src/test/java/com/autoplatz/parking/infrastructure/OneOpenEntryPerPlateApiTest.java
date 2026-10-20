package com.autoplatz.parking.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

/**
 * The API end to end: a plate with an entry still inside is refused a second one, and nothing more is stored.
 * It adds entries, so the context (and its PostgreSQL) is not shared with the tests after it.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@DirtiesContext
class OneOpenEntryPerPlateApiTest {
  @Autowired MockMvc mvc;
  @Autowired JdbcClient jdbc;

  @Test
  @DisplayName("REQ-016 SCN-027: a second entry for \"AB123CD\" while it is inside is refused with \"Vehicle is already in the garage\"")
  void aVehicleStillInsideCannotEnterAgain() throws Exception {
    String body = "{\"licensePlate\":\"AB123CD\",\"vehicleTypeId\":\"" + idOf("Car") + "\"}";
    mvc.perform(post("/api/entries").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isCreated());
    assertThat(insideWithPlate("AB123CD")).isEqualTo(1);

    mvc.perform(post("/api/entries").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.code").value("vehicle_already_in_garage"))
        .andExpect(jsonPath("$.detail").value("Vehicle is already in the garage"));

    assertThat(insideWithPlate("AB123CD")).isEqualTo(1);
  }

  private long insideWithPlate(String plate) {
    return jdbc.sql("SELECT count(*) FROM parking_entry WHERE license_plate = :plate AND status = 'inside'")
        .param("plate", plate).query(Long.class).single();
  }

  private String idOf(String name) throws Exception {
    String json = mvc.perform(get("/api/vehicle-types")).andReturn().getResponse().getContentAsString();
    List<String> ids = JsonPath.read(json, "$[?(@.name == '" + name + "')].id");
    return ids.getFirst();
  }
}
