package com.autoplatz.parking.infrastructure;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.autoplatz.TestcontainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

/**
 * The API end to end: a type added to the seeded catalog is active and offered in the entry form.
 * It changes the catalog, so the context (and its PostgreSQL) is not shared with the tests after it.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@DirtiesContext
class AddVehicleTypeApiTest {
  @Autowired MockMvc mvc;

  @Test
  @DisplayName("REQ-003 SCN-003: a type with a unique name becomes available in the entry form")
  void anAddedTypeBecomesAvailableInTheEntryForm() throws Exception {
    mvc.perform(get("/api/vehicle-types")).andExpect(status().isOk())
        .andExpect(jsonPath("$[*].name", contains("Car", "Motorcycle", "Van")));

    mvc.perform(post("/api/vehicle-types").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Truck\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("Truck")).andExpect(jsonPath("$.active").value(true));

    mvc.perform(get("/api/vehicle-types")).andExpect(status().isOk())
        .andExpect(jsonPath("$[?(@.name == 'Truck')].active", contains(true)));
    mvc.perform(get("/api/vehicle-types/available")).andExpect(status().isOk())
        .andExpect(jsonPath("$[*].name", containsInAnyOrder("Car", "Motorcycle", "Van", "Truck")));
  }

  @Test
  @DisplayName("REQ-003: an empty name is a problem detail and adds nothing")
  void anEmptyNameIsRejected() throws Exception {
    mvc.perform(post("/api/vehicle-types").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"  \"}"))
        .andExpect(status().isUnprocessableContent()).andExpect(jsonPath("$.code").value("invalid_vehicle_type_name"));
  }

  @Test
  @DisplayName("REQ-004 SCN-004 SCN-005 SCN-020: a duplicate or empty name is rejected and the catalog is unchanged")
  void aDuplicateOrEmptyNameIsRejected() throws Exception {
    mvc.perform(post("/api/vehicle-types").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Car\"}"))
        .andExpect(status().isConflict()).andExpect(jsonPath("$.code").value("duplicate_vehicle_type_name"))
        .andExpect(jsonPath("$.detail", containsString("already exists")));
    mvc.perform(post("/api/vehicle-types").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\" car \"}"))
        .andExpect(status().isConflict()).andExpect(jsonPath("$.code").value("duplicate_vehicle_type_name"));
    mvc.perform(post("/api/vehicle-types").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"\"}"))
        .andExpect(status().isUnprocessableContent()).andExpect(jsonPath("$.code").value("invalid_vehicle_type_name"))
        .andExpect(jsonPath("$.detail", containsString("required")));

    mvc.perform(get("/api/vehicle-types")).andExpect(status().isOk())
        .andExpect(jsonPath("$[?(@.name == 'Car')]", hasSize(1)))
        .andExpect(jsonPath("$[*].name", hasItems("Car", "Motorcycle", "Van")));
  }
}
