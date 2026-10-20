package com.autoplatz.tasks.application;

import com.autoplatz.shared.application.CurrentUser;
import com.autoplatz.tasks.application.port.TaskRepository;
import com.autoplatz.tasks.domain.Task;
import java.util.List;

public final class ListMyTasks {
  private final TaskRepository tasks;
  private final CurrentUser user;

  public ListMyTasks(TaskRepository tasks, CurrentUser user) {
    this.tasks = tasks;
    this.user = user;
  }

  public List<Task> execute() {
    return tasks.findByOwner(user.id());
  }
}
