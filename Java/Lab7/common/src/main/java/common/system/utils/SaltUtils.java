package common.system.utils;

import java.security.SecureRandom;

public class SaltUtils {
  public static String generateSalt(int length) {
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[length];
    random.nextBytes(salt);
    StringBuilder sb = new StringBuilder();
    for (byte b : salt) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }
}
