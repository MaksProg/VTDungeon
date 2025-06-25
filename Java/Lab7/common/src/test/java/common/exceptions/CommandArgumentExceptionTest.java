package common.exceptions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CommandArgumentExceptionTest {

  @Test
  void testDefaultConstructor() {
    CommandArgumentException ex = new CommandArgumentException();
    assertNull(ex.getMessage());
  }

  @Test
  void testMessageConstructor() {
    CommandArgumentException ex = new CommandArgumentException("Сообщение");
    assertEquals("Сообщение", ex.getMessage());
  }

  @Test
  void testCauseConstructor() {
    Throwable cause = new RuntimeException("Причина");
    CommandArgumentException ex = new CommandArgumentException(cause);
    assertEquals(cause, ex.getCause());
  }

  @Test
  void testMessageAndCauseConstructor() {
    Throwable cause = new RuntimeException("Причина");
    CommandArgumentException ex = new CommandArgumentException("Сообщение", cause);
    assertEquals("Сообщение", ex.getMessage());
    assertEquals(cause, ex.getCause());
  }

  @Test
  void testFullConstructor() {
    Throwable cause = new RuntimeException("Причина");
    CommandArgumentException ex = new CommandArgumentException("Сообщение", cause, true, true);
    assertEquals("Сообщение", ex.getMessage());
    assertEquals(cause, ex.getCause());
    assertTrue(ex.getSuppressed().length == 0);
  }

  @Test
  void testCustomConstructors() {
    CommandArgumentException ex1 = new CommandArgumentException("cmd", 2, 5);
    assertEquals("cmd принимает 2 аргумент(ов). Получено 5", ex1.getMessage());

    CommandArgumentException ex2 = new CommandArgumentException("cmd", 3);
    assertEquals("cmd не принимает аргументов. Получено 3", ex2.getMessage());
  }
}
