package client;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

class FakeChannelWrapper extends ObjectSocketChannelWrapper {
  private final Queue<Object> responses = new ArrayDeque<>();
  private final List<Object> sentMessages = new ArrayList<>();

  public FakeChannelWrapper() {
    super(null);
  }

  @Override
  public void sendMessage(Object object) {
    sentMessages.add(object);
  }

  @Override
  public boolean checkForMessage() {
    return !responses.isEmpty();
  }

  @Override
  public Object getPayload() {
    return responses.poll();
  }

  @Override
  public void clearInBuffer() {}

  public void addMockResponse(Object obj) {
    responses.add(obj);
  }

  public List<Object> getSentMessages() {
    return sentMessages;
  }
}
