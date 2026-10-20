package com.autoplatz.tasks.infrastructure;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.autoplatz.TestcontainersConfiguration;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

/** The API end to end: HTTP → use case → adapter JDBC (real PostgreSQL). */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class TaskApiTest {
  @Autowired MockMvc mvc;

  private final String ana = "local-user";

  private MockHttpServletRequestBuilder as(String user, MockHttpServletRequestBuilder request) {
    return request;
  }

  private String create(String user, String title) throws Exception {
    String body = mvc.perform(as(user, post("/api/tasks")).contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\"" + title + "\"}"))
        .andExpect(status().isCreated()).andExpect(jsonPath("$.done").value(false))
        .andReturn().getResponse().getContentAsString();
    return JsonPath.read(body, "$.id");
  }

  @Test
  void createListAndComplete() throws Exception {
    String id = create(ana, "Prepare the demo");
    mvc.perform(as(ana, get("/api/tasks"))).andExpect(status().isOk())
        .andExpect(jsonPath("$[?(@.id == '" + id + "')].title").value("Prepare the demo"));
    mvc.perform(as(ana, post("/api/tasks/" + id + "/completion"))).andExpect(status().isOk())
        .andExpect(jsonPath("$.done").value(true));
    mvc.perform(as(ana, post("/api/tasks/" + id + "/completion"))).andExpect(status().isConflict())
        .andExpect(jsonPath("$.code").value("task_already_done"));
  }

  @Test
  void errorsAreProblemDetails() throws Exception {
    mvc.perform(as(ana, post("/api/tasks")).contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"  \"}"))
        .andExpect(status().isUnprocessableContent()).andExpect(jsonPath("$.code").value("invalid_title"));
    mvc.perform(as(ana, post("/api/tasks")).contentType(MediaType.APPLICATION_JSON).content("{\"title\":"))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value("invalid_request"));
    mvc.perform(as(ana, post("/api/tasks/not-an-id/completion"))).andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("task_not_found"));
  }

  @Test
  void healthAndOpenApiAreOpen() throws Exception {
    mvc.perform(get("/actuator/health")).andExpect(status().isOk());
    mvc.perform(get("/v3/api-docs")).andExpect(status().isOk()).andExpect(jsonPath("$.paths['/api/tasks']").exists());
  }
}
