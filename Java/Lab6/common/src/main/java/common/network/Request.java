package common.network;

import java.io.Serializable;

public class Request implements Serializable {
  private static final long serialVersionUID = 4643797287519810631L;
  private String commandName;
  private RequestBody requestBody;

  public Request(String commandName, RequestBody requestBody) {
    this.commandName = commandName;
    this.requestBody = requestBody;
  }

  public String getCommandName() {
    return commandName;
  }

  public RequestBody getRequestBody() {
    return requestBody;
  }
}
