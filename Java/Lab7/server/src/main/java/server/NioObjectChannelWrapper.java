package server;

import common.network.ObjectEncoder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Обёртка для {@link SocketChannel}, реализующая неблокирующий ввод-вывод с поддержкой передачи
 * сериализованных объектов.
 *
 * <p>Протокол передачи:
 *
 * <ul>
 *   <li>Сначала читается 4-байтовое целое число — размер сообщения (payload).
 *   <li>Затем читается полезная нагрузка указанного размера.
 *   <li>Для отправки объектов данные сериализуются, размер передается вместе с сериализованным
 *       объектом.
 * </ul>
 *
 * <p>Основные методы:
 *
 * <ul>
 *   <li>{@link #checkForMessage()} — проверяет, пришло ли полное сообщение.
 *   <li>{@link #getPayload()} — десериализует и возвращает объект из буфера.
 *   <li>{@link #sendMessage(Object)} — сериализует и отправляет объект.
 *   <li>{@link #clearInBuffer()} — очищает внутренние буферы после обработки сообщения.
 * </ul>
 */
public class NioObjectChannelWrapper {
  private static final int SIZE_BYTES = Integer.BYTES;

  private final SocketChannel channel;
  final ByteBuffer sizeBuffer = ByteBuffer.allocate(SIZE_BYTES);
  ByteBuffer payloadBuffer = null;

  /**
   * Создаёт обёртку вокруг заданного {@link SocketChannel}.
   *
   * @param channel канал для неблокирующего ввода-вывода
   */
  public NioObjectChannelWrapper(SocketChannel channel) {
    this.channel = channel;
  }

  /**
   * Пытается прочитать из канала сообщение.
   *
   * <p>Если ещё не прочитан размер сообщения, пытается его считать. Если размер прочитан, пытается
   * считать полезную нагрузку.
   *
   * @return {@code true}, если всё сообщение полностью прочитано, иначе {@code false}
   * @throws IOException если соединение было закрыто или произошла ошибка ввода-вывода
   */
  public boolean checkForMessage() throws IOException {
    if (payloadBuffer == null) {
      int read = channel.read(sizeBuffer);
      if (read == -1) throw new IOException("Клиент закрыл подключение");
      if (sizeBuffer.hasRemaining()) return false;

      sizeBuffer.flip();
      int payloadSize = sizeBuffer.getInt();
      payloadBuffer = ByteBuffer.allocate(payloadSize);
    }

    int read = channel.read(payloadBuffer);
    if (read == -1) throw new IOException("Клиент закрыл подключение");
    return !payloadBuffer.hasRemaining();
  }

  /**
   * Десериализует и возвращает объект из полученного сообщения.
   *
   * @return десериализованный объект
   * @throws IOException при ошибках ввода-вывода
   * @throws ClassNotFoundException если класс объекта не найден
   */
  public Object getPayload() throws IOException, ClassNotFoundException {
    payloadBuffer.flip();
    byte[] data = new byte[payloadBuffer.remaining()];
    payloadBuffer.get(data);

    try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
      return ois.readObject();
    }
  }

  /** Очищает внутренние буферы для приёма следующего сообщения. */
  public void clearInBuffer() {
    sizeBuffer.clear();
    payloadBuffer = null;
  }

  /**
   * Сериализует и отправляет объект через {@link SocketChannel}.
   *
   * @param obj объект для отправки
   * @throws IOException при ошибках записи в канал
   */
  public void sendMessage(Object obj) throws IOException {
    ByteBuffer data = ObjectEncoder.encodeObject(obj);
    data.rewind();
    while (data.hasRemaining()) {
      channel.write(data);
    }
  }

  /**
   * Возвращает канал, с которым работает эта обёртка.
   *
   * @return исходный {@link SocketChannel}
   */
  public SocketChannel getChannel() {
    return channel;
  }
}
