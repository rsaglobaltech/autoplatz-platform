package com.autoplatz;

import static org.assertj.core.api.Assertions.assertThat;

import com.autoplatz.shared.application.CurrentUser;
import com.autoplatz.tasks.application.port.TaskRepository;
import java.time.Clock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * The application starts with its real pieces, no test doubles. Other tests replace pieces;
 * this one checks that the real ones exist and are wired (green tests with an app that does not start is a
 * failure that has already happened).
 */
@SpringBootTest
@Import(TestcontainersConfiguration.class)
class StartupTest {
  @Autowired TaskRepository tasks;
  @Autowired CurrentUser user;
  @Autowired Clock clock;

  @Test
  void theRealPiecesAreWired() {
    assertThat(tasks.getClass().getSimpleName()).startsWith("JdbcTaskRepository");
    assertThat(user.getClass().getSimpleName()).startsWith("LocalUser");
    assertThat(clock.getZone().getId()).isEqualTo("Z");
  }
}
