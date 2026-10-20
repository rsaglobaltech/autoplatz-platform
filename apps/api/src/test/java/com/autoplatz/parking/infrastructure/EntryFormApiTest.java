package com.autoplatz.parking.infrastructure;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
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

/** The entry form over HTTP: a fresh PostgreSQL, the catalog seeded with Car, Motorcycle and Van. */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class EntryFormApiTest {
  @Autowired MockMvc mvc;

  @Test
  @DisplayName("REQ-012 SCN-022: the entry form has a required plate, a required type from the catalog, an optional owner and the entry date/time")
  void theEntryFormShowsItsFields() throws Exception {
    mvc.perform(get("/api/entries/form")).andExpect(status().isOk())
        .andExpect(jsonPath("$.fields[*].name", contains("licensePlate", "vehicleType", "ownerName", "enteredAt")))
        .andExpect(jsonPath("$.fields[0].required").value(true))
        .andExpect(jsonPath("$.fields[1].required").value(true))
        .andExpect(jsonPath("$.fields[1].options[*].name", hasItem("Car")))
        .andExpect(jsonPath("$.fields[2].required").value(false));
  }
}
