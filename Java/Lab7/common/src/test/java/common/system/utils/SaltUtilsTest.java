package common.system.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SaltUtilsTest {

  @Test
  void generateSalt_lengthIsCorrect() {
    int length = 16;
    String salt = SaltUtils.generateSalt(length);

    assertEquals(length * 2, salt.length());
  }

  @Test
  void generateSalt_containsOnlyHexCharacters() {
    String salt = SaltUtils.generateSalt(20);
    assertTrue(salt.matches("[0-9a-f]+"), "Salt содержит не hex символы");
  }

  @Test
  void generateSalt_differentValues() {
    String salt1 = SaltUtils.generateSalt(10);
    String salt2 = SaltUtils.generateSalt(10);
    assertNotEquals(
        salt1, salt2, "Два разных вызова generateSalt не должны возвращать одинаковые значения");
  }
}
