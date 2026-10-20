package com.autoplatz.tasks.application.port;

import com.autoplatz.tasks.domain.Task;
import com.autoplatz.tasks.domain.TaskId;
import java.util.List;
import java.util.Optional;

/** Outbound port: where tasks live. Adapters in infrastructure/persistence. */
public interface TaskRepository {
  void save(Task task);

  Optional<Task> findById(TaskId id);

  /** The user's tasks, newest first. */
  List<Task> findByOwner(String owner);
}
