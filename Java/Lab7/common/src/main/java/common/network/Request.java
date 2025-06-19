package common.network;

import common.data.auth.AuthCredentials;
import java.io.Serializable;

public class Request implements Serializable {
  private static final long serialVersionUID = 4643797287519810631L;
  private String commandName;
  private RequestBody requestBody;
  private AuthCredentials auth;

  public Request(String commandName, RequestBody requestBody, AuthCredentials auth) {
    this.commandName = commandName;
    this.requestBody = requestBody;
    this.auth = auth;
  }

  public String getCommandName() {
    return commandName;
  }

  public RequestBody getRequestBody() {
    return requestBody;
  }

  public AuthCredentials getAuth() {
    return auth;
  }
}
