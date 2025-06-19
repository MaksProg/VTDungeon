package common.system.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class PassWordUtils {
  public static String hashPassword(String password, String salt) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-224");
      byte[] hash = messageDigest.digest((password + salt).getBytes(StandardCharsets.UTF_8));
      StringBuilder hex = new StringBuilder();
      for (byte b : hash) {
        hex.append(String.format("%02x", b));
      }
      return hex.toString();
    } catch (Exception e) {
      throw new RuntimeException("Ошибка хэширования пароля", e);
    }
  }
}
