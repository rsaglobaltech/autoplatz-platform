package com.autoplatz.parking.infrastructure;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.autoplatz.TestcontainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

/** The API end to end: a fresh PostgreSQL, the application starts, the catalog is seeded. */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class VehicleTypeApiTest {
  @Autowired MockMvc mvc;

  @Test
  @DisplayName("REQ-002 SCN-002: after the first start the catalog holds Car, Motorcycle and Van, active and in the entry form")
  void freshInstallationSeedsTheDefaultTypes() throws Exception {
    mvc.perform(get("/api/vehicle-types")).andExpect(status().isOk())
        .andExpect(jsonPath("$[*].name", contains("Car", "Motorcycle", "Van")))
        .andExpect(jsonPath("$[*].active", everyItem(is(true))));
    mvc.perform(get("/api/vehicle-types/available")).andExpect(status().isOk())
        .andExpect(jsonPath("$[*].name", contains("Car", "Motorcycle", "Van")));
  }
}
