package common.data.auth;

import java.io.Serializable;

public class AuthCredentials implements Serializable {
  private final String username;
  private final String password;

  public AuthCredentials(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String username() {
    return username;
  }

  public String password() {
    return password;
  }
}
