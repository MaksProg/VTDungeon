package common.network;

import java.util.ArrayDeque;

public class RequestBodyWithHistory extends RequestBody {
  private final ArrayDeque<String> history;
  private static final long serialVersionUID = -5519361423588780166L;

  public RequestBodyWithHistory(String[] args, ArrayDeque<String> history) {
    super(args);
    this.history = history;
  }

  public ArrayDeque<String> getHistory() {
    return history;
  }
}
