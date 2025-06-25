package common.system.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PassWordUtilsTest {

  @Test
  void hashPassword_sameInput_sameOutput() {
    String password = "password123";
    String salt = "saltValue";

    String hash1 = PassWordUtils.hashPassword(password, salt);
    String hash2 = PassWordUtils.hashPassword(password, salt);

    assertEquals(hash1, hash2, "Хэши для одинаковых входных данных должны совпадать");
  }

  @Test
  void hashPassword_differentSalt_differentOutput() {
    String password = "password123";
    String salt1 = "saltOne";
    String salt2 = "saltTwo";

    String hash1 = PassWordUtils.hashPassword(password, salt1);
    String hash2 = PassWordUtils.hashPassword(password, salt2);

    assertNotEquals(hash1, hash2, "Хэши с разными солями должны отличаться");
  }

  @Test
  void hashPassword_differentPassword_differentOutput() {
    String password1 = "password123";
    String password2 = "password124";
    String salt = "saltValue";

    String hash1 = PassWordUtils.hashPassword(password1, salt);
    String hash2 = PassWordUtils.hashPassword(password2, salt);

    assertNotEquals(hash1, hash2, "Хэши для разных паролей должны отличаться");
  }

  @Test
  void hashPassword_emptyPasswordAndSalt() {
    String hash = PassWordUtils.hashPassword("", "");
    assertNotNull(hash, "Хэш не должен быть null для пустых строк");
    assertFalse(hash.isEmpty(), "Хэш не должен быть пустой строкой");
  }

  @Test
  void hashPassword_nullPasswordOrSalt_throwsException() {
    assertThrows(RuntimeException.class, () -> PassWordUtils.hashPassword(null, "salt"));
    assertThrows(RuntimeException.class, () -> PassWordUtils.hashPassword("password", null));
  }
}
