package com.autoplatz.tasks.infrastructure.config;

import com.autoplatz.shared.application.CurrentUser;
import com.autoplatz.tasks.application.CompleteTask;
import com.autoplatz.tasks.application.CreateTask;
import com.autoplatz.tasks.application.ListMyTasks;
import com.autoplatz.tasks.application.port.TaskRepository;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** The only place that knows both the use cases and Spring: they are wired here. */
@Configuration(proxyBeanMethods = false)
class TasksConfiguration {
  @Bean
  CreateTask createTask(TaskRepository tasks, CurrentUser user, Clock clock) {
    return new CreateTask(tasks, user, clock);
  }

  @Bean
  ListMyTasks listMyTasks(TaskRepository tasks, CurrentUser user) {
    return new ListMyTasks(tasks, user);
  }

  @Bean
  CompleteTask completeTask(TaskRepository tasks, CurrentUser user, Clock clock) {
    return new CompleteTask(tasks, user, clock);
  }
}
