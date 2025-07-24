package common.managers;

import static org.junit.jupiter.api.Assertions.*;

import common.data.auth.AuthCredentials;
import common.exceptions.UnknownCommandException;
import common.network.Request;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommandManagerTest {
  private FakeCommandManager manager;

  @BeforeEach
  void setUp() {
    manager = new FakeCommandManager();
  }

  @Test
  void testAddToHistory() {
    manager.addToHistory("mock1");
    manager.addToHistory("mock2");
    manager.addToHistory("mock3");
    manager.addToHistory("mock4");
    manager.addToHistory("mock5");
    manager.addToHistory("mock6");
    manager.addToHistory("mock7");

    assertEquals(6, manager.getHistory().size());
    assertFalse(manager.getHistory().contains("mock1"));
    assertTrue(manager.getHistory().contains("mock7"));
  }

  @Test
  void testHandleCommandInput_valid() throws Exception {
    Scanner scanner = new Scanner(System.in);
    AuthCredentials auth = new AuthCredentials("test", "pass");

    Request req = manager.handleCommandInput("mock", scanner, auth);

    assertNotNull(req);
    assertEquals("mock", req.getCommandName());
    assertEquals(auth, req.getAuth());
  }

  @Test
  void testHandleCommandInput_unknown() {
    Scanner scanner = new Scanner(System.in);
    AuthCredentials auth = new AuthCredentials("test", "pass");

    assertThrows(
        UnknownCommandException.class,
        () -> {
          manager.handleCommandInput("unknown", scanner, auth);
        });
  }
}
