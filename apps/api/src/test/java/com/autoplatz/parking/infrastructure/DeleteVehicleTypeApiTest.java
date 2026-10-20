package com.autoplatz.parking.infrastructure;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
 * The API end to end: deleting a type in use deactivates it, deleting an unused one removes it.
 * It changes the catalog, so the context (and its PostgreSQL) is not shared with the tests after it.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@DirtiesContext
class DeleteVehicleTypeApiTest {
  @Autowired MockMvc mvc;
  @Autowired JdbcClient jdbc;

  @Test
  @DisplayName("REQ-005 SCN-006: deleting a used type deactivates it, the form hides it, the history keeps it")
  void deletingAUsedTypeDeactivatesIt() throws Exception {
    String van = idOf("Van");
    UUID entry = UUID.randomUUID();
    jdbc.sql("INSERT INTO parking_entry (id, vehicle_type_id) VALUES (:id, :type)")
        .param("id", entry).param("type", UUID.fromString(van)).update();

    mvc.perform(delete("/api/vehicle-types/{id}", van)).andExpect(status().isNoContent());

    mvc.perform(get("/api/vehicle-types")).andExpect(status().isOk())
        .andExpect(jsonPath("$[?(@.name == 'Van')].active", contains(false)));
    mvc.perform(get("/api/vehicle-types/available")).andExpect(status().isOk())
        .andExpect(jsonPath("$[*].name", not(hasItem("Van"))));
    mvc.perform(get("/api/entries")).andExpect(status().isOk())
        .andExpect(jsonPath("$[?(@.id == '" + entry + "')].vehicleType", contains("Van")));
  }

  @Test
  @DisplayName("REQ-005 SCN-021: deleting a type no entry uses removes it from the catalog and the form")
  void deletingAnUnusedTypeRemovesIt() throws Exception {
    String body = mvc.perform(post("/api/vehicle-types").contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"Truck\"}"))
        .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
    String truck = JsonPath.read(body, "$.id");

    mvc.perform(delete("/api/vehicle-types/{id}", truck)).andExpect(status().isNoContent());

    mvc.perform(get("/api/vehicle-types")).andExpect(status().isOk())
        .andExpect(jsonPath("$[?(@.name == 'Truck')]", empty()));
    mvc.perform(get("/api/vehicle-types/available")).andExpect(status().isOk())
        .andExpect(jsonPath("$[*].name", not(hasItem("Truck"))));
  }

  @Test
  @DisplayName("REQ-005: deleting a type that does not exist is a problem detail")
  void deletingAnUnknownTypeIsNotFound() throws Exception {
    mvc.perform(delete("/api/vehicle-types/{id}", UUID.randomUUID()))
        .andExpect(status().isNotFound()).andExpect(jsonPath("$.code").value("vehicle_type_not_found"));
  }

  private String idOf(String name) throws Exception {
    String body = mvc.perform(get("/api/vehicle-types")).andReturn().getResponse().getContentAsString();
    return JsonPath.<List<String>>read(body, "$[?(@.name == '" + name + "')].id").getFirst();
  }
}
