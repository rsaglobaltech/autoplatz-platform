package com.autoplatz.tasks.application;

import com.autoplatz.shared.application.CurrentUser;
import com.autoplatz.tasks.application.port.TaskRepository;
import com.autoplatz.tasks.domain.Task;
import com.autoplatz.tasks.domain.TaskTitle;
import java.time.Clock;

/** Use case: create a task for the current user. No annotations: it is instantiated in infrastructure/config. */
public final class CreateTask {
  private final TaskRepository tasks;
  private final CurrentUser user;
  private final Clock clock;

  public CreateTask(TaskRepository tasks, CurrentUser user, Clock clock) {
    this.tasks = tasks;
    this.user = user;
    this.clock = clock;
  }

  public Task execute(String title) {
    Task task = Task.create(user.id(), new TaskTitle(title), clock.instant());
    tasks.save(task);
    return task;
  }
}
