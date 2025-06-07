package client;

import common.network.ObjectEncoder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ObjectSocketChannelWrapper {
  private final SocketChannel socket;
  private final ByteBuffer sizeBuffer = ByteBuffer.allocate(Integer.BYTES);
  private ByteBuffer payloadBuffer = null;

  public ObjectSocketChannelWrapper(SocketChannel socket) {
    this.socket = socket;
  }

  /** Отправляет объект по сокету в сериализованном виде. */
  public void sendMessage(Object object) throws IOException {
    ByteBuffer buffer = ObjectEncoder.encodeObject(object);
    buffer.flip();

    while (buffer.hasRemaining()) {
      socket.write(buffer);
    }
  }

  /** Проверяет, пришло ли новое сообщение. Сначала читает размер, потом полезную нагрузку. */
  public boolean checkForMessage() throws IOException {
    if (payloadBuffer != null && !payloadBuffer.hasRemaining()) {
      return true;
    }

    if (sizeBuffer.hasRemaining()) {
      socket.read(sizeBuffer);
      if (sizeBuffer.hasRemaining()) {
        return false;
      }
    }

    if (payloadBuffer == null) {
      int size = sizeBuffer.getInt(0);
      payloadBuffer = ByteBuffer.allocate(size);
    }

    socket.read(payloadBuffer);
    return !payloadBuffer.hasRemaining();
  }

  /** Возвращает десериализованный объект. */
  public Object getPayload() throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(payloadBuffer.array());
    try (ObjectInputStream ois = new ObjectInputStream(bais)) {
      return ois.readObject();
    } catch (ClassNotFoundException e) {
      return null;
    }
  }

  /** Очищает буферы после получения объекта. */
  public void clearInBuffer() {
    sizeBuffer.clear();
    payloadBuffer = null;
  }

  public SocketChannel getSocket() {
    return socket;
  }
}
