package common.network;

import java.io.Serializable;

public class Response implements Serializable {
  private static final long serialVersionUID = 1L;
  private Object result;
  private boolean shouldStop;

  private String message = "";

  public Response() {}

  public Response(String message) {
    this.message = message;
  }

  public Response(String message, Object result) {
    this.message = message;
    this.result = result;
  }

  public Response(String message, boolean shouldStop) {
    this.message = message;
    this.shouldStop = shouldStop;
  }

  public boolean shouldStopClient() {
    return shouldStop;
  }

  public String getMessage() {
    return message;
  }

  public Object getResult() {
    return result;
  }
}
