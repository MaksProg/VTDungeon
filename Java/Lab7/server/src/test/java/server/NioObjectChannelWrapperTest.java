package server;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class NioObjectChannelWrapperTest {

  private static ServerSocketChannel server;
  private static ExecutorService executor;

  @BeforeAll
  static void setupServer() throws IOException {
    server = ServerSocketChannel.open();
    server.bind(new InetSocketAddress("localhost", 0));
    executor = Executors.newSingleThreadExecutor();
  }

  @AfterAll
  static void cleanup() throws IOException {
    if (server != null) server.close();
    if (executor != null) executor.shutdownNow();
  }

  @Test
  void testSendMessageCheckForMessageGetPayloadClearInBuffer() throws Exception {
    int port = ((InetSocketAddress) server.getLocalAddress()).getPort();

    Future<NioObjectChannelWrapper> serverWrapperFuture =
        executor.submit(
            () -> {
              SocketChannel accepted = server.accept();
              accepted.configureBlocking(true);
              return new NioObjectChannelWrapper(accepted);
            });

    SocketChannel clientChannel = SocketChannel.open();
    clientChannel.connect(new InetSocketAddress("localhost", port));
    clientChannel.configureBlocking(true);

    NioObjectChannelWrapper clientWrapper = new NioObjectChannelWrapper(clientChannel);
    NioObjectChannelWrapper serverWrapper = serverWrapperFuture.get(5, TimeUnit.SECONDS);

    assertSame(clientChannel, clientWrapper.getChannel());
    assertNotNull(serverWrapper.getChannel());

    String testMessage = "Hello, NioObjectChannelWrapper!";

    clientWrapper.sendMessage(testMessage);

    boolean complete = false;
    for (int i = 0; i < 100; i++) {
      if (serverWrapper.checkForMessage()) {
        complete = true;
        break;
      }
      Thread.sleep(10);
    }
    assertTrue(complete, "Сообщение не было прочитано полностью");

    Object payload = serverWrapper.getPayload();
    assertEquals(testMessage, payload);

    serverWrapper.clearInBuffer();

    assertFalse(serverWrapper.sizeBuffer.hasRemaining() == false, "sizeBuffer должен быть очищен");

    clientChannel.close();
    serverWrapper.getChannel().close();
  }
}
