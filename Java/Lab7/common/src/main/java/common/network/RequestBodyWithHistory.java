package common.network;

import java.util.concurrent.ConcurrentLinkedDeque;

public class RequestBodyWithHistory extends RequestBody {
  private final ConcurrentLinkedDeque<String> history;

  public RequestBodyWithHistory(String[] args, ConcurrentLinkedDeque<String> history) {
    super(args);
    this.history = history;
  }

  public ConcurrentLinkedDeque<String> getHistory() {
    return history;
  }
}
