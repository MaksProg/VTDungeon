package client;

import static org.junit.jupiter.api.Assertions.*;

import common.network.ObjectEncoder;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ObjectSocketChannelWrapperTest {

  private ServerSocketChannel serverSocket;
  private SocketChannel serverChannel;
  private SocketChannel clientChannel;
  private ObjectSocketChannelWrapper wrapper;

  @BeforeEach
  public void setUp() throws IOException {
    // Открываем серверный канал на свободном порту
    serverSocket = ServerSocketChannel.open();
    serverSocket.bind(new InetSocketAddress(0)); // 0 — свободный порт

    // Адрес сервера
    InetSocketAddress address = (InetSocketAddress) serverSocket.getLocalAddress();

    // Создаём клиентский канал и подключаем его
    clientChannel = SocketChannel.open();
    clientChannel.configureBlocking(false);
    clientChannel.connect(address);

    // Принять подключение сервером
    serverChannel = serverSocket.accept();
    serverChannel.configureBlocking(false);

    // Ждём пока клиент подключится
    while (!clientChannel.finishConnect()) {
      // ожидание подключения
    }

    wrapper = new ObjectSocketChannelWrapper(clientChannel);
  }

  @AfterEach
  public void tearDown() throws IOException {
    if (serverChannel != null) serverChannel.close();
    if (clientChannel != null) clientChannel.close();
    if (serverSocket != null) serverSocket.close();
  }

  @Test
  public void testCheckForMessageAndGetPayload() throws IOException, InterruptedException {
    String testMessage = "Hello, test!";

    Thread writerThread =
        new Thread(
            () -> {
              try {
                ByteBuffer buffer = ObjectEncoder.encodeObject(testMessage);
                buffer.flip();

                while (buffer.hasRemaining()) {
                  serverChannel.write(buffer);
                }
              } catch (IOException e) {
                e.printStackTrace();
                fail("Ошибка записи данных в канал");
              }
            });
    writerThread.start();

    boolean ready = false;
    long start = System.currentTimeMillis();
    long timeout = 5000;

    while (!ready && (System.currentTimeMillis() - start) < timeout) {
      ready = wrapper.checkForMessage();
      Thread.sleep(50);
    }

    assertTrue(ready, "Payload должен быть прочитан за 5 секунд");

    Object payload = wrapper.getPayload();
    assertEquals(testMessage, payload);

    wrapper.clearInBuffer();

    writerThread.join();
  }
}
