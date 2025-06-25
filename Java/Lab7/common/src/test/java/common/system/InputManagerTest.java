package common.system;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InputManagerTest {

  private Scanner testScanner;

  @BeforeEach
  void setup() {
    String input = "42\n-1\n100\nabc\n50\n";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    testScanner = new Scanner(inputStream);
    InputManager.setScanner(testScanner);
  }

  @Test
  void promptValid_returnsValidParsedValue() {
    Function<String, Integer> parser = Integer::parseInt;
    Predicate<Integer> validator = i -> i > 0;

    int result =
        InputManager.promptValid(
            testScanner,
            "Введите число > 0: ",
            parser,
            validator,
            "Число должно быть положительным");

    assertEquals(42, result);
  }

  @Test
  void promptValid_skipsInvalidInputs_untilValid() {
    Function<String, Integer> parser = Integer::parseInt;
    Predicate<Integer> validator = i -> i > 40;

    int result =
        InputManager.promptValid(
            testScanner, "Введите число > 40: ", parser, validator, "Число должно быть > 40");

    // Первый ввод 42 — подходит, вернётся он
    assertEquals(42, result);
  }

  @Test
  void safeNextLine_readsFromScannerOrFallback() {
    assertEquals("42", InputManager.safeNextLine());
    assertEquals("-1", InputManager.safeNextLine());
    assertEquals("100", InputManager.safeNextLine());
    assertEquals("abc", InputManager.safeNextLine());
    assertEquals("50", InputManager.safeNextLine());
  }

  @Test
  void setAndGetScanner_worksCorrectly() {
    Scanner newScanner = new Scanner("test\n");
    InputManager.setScanner(newScanner);
    assertSame(newScanner, InputManager.getScanner());
  }

  @Test
  void nextLineAndHasNextLine_worksWithCurrentScanner() {
    InputManager.setScanner(new Scanner("line1\nline2\n"));

    assertTrue(InputManager.hasNextLine());
    assertEquals("line1", InputManager.nextLine());
    assertTrue(InputManager.hasNextLine());
    assertEquals("line2", InputManager.nextLine());
    assertFalse(InputManager.hasNextLine());
  }
}
