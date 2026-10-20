package com.autoplatz.tasks.application;

import com.autoplatz.shared.application.CurrentUser;
import com.autoplatz.tasks.application.port.TaskRepository;
import com.autoplatz.tasks.domain.Task;
import com.autoplatz.tasks.domain.TaskId;
import com.autoplatz.tasks.domain.TaskNotFound;
import java.time.Clock;

/** Another user's task responds as if it did not exist: which ids exist is never revealed. */
public final class CompleteTask {
  private final TaskRepository tasks;
  private final CurrentUser user;
  private final Clock clock;

  public CompleteTask(TaskRepository tasks, CurrentUser user, Clock clock) {
    this.tasks = tasks;
    this.user = user;
    this.clock = clock;
  }

  public Task execute(String id) {
    TaskId taskId = TaskId.of(id);
    Task task = tasks.findById(taskId).filter(t -> t.ownedBy(user.id())).orElseThrow(() -> TaskNotFound.withId(id));
    Task done = task.complete(clock.instant());
    tasks.save(done);
    return done;
  }
}
