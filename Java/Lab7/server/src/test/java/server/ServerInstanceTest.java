package server;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import common.data.auth.AuthCredentials;
import common.managers.CollectionManager;
import common.managers.ServerCommandManager;
import common.managers.auth.UserManager;
import common.network.Request;
import common.network.Response;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;
import org.junit.jupiter.api.*;

public class ServerInstanceTest {

  private ServerInstance serverInstance;
  private CollectionManager collectionManager;
  private UserManager userManager;
  private ServerCommandManager commandManager;

  private ExecutorService serverExecutor;

  private static final int TEST_PORT = 12345;

  @BeforeEach
  public void setup() {
    collectionManager = mock(CollectionManager.class);
    userManager = mock(UserManager.class);

    commandManager = new ServerCommandManager(collectionManager, userManager);
    serverInstance = new ServerInstance(commandManager, collectionManager, null);

    serverExecutor = Executors.newSingleThreadExecutor();
  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    if (serverExecutor != null) {
      serverExecutor.shutdownNow();
      serverExecutor.awaitTermination(1, TimeUnit.SECONDS);
    }
  }

  @Test
  public void testHandleRequestDirectly() {
    Request fakeRequest = new Request("help", null, null);
    Response response = commandManager.handleRequest(fakeRequest);
    assertNotNull(response);
    assertFalse(response.getMessage().isEmpty());
  }

  @Test
  public void testRunAndExchangeManually() throws Exception {
    serverExecutor.submit(
        () -> {
          try {
            serverInstance.run(TEST_PORT);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });

    Thread.sleep(300);

    try (SocketChannel channel =
        SocketChannel.open(new InetSocketAddress("localhost", TEST_PORT))) {
      channel.configureBlocking(true);

      ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(byteOut);
      oos.writeObject(new Request("help", null, new AuthCredentials("user", "pass")));
      oos.flush();

      byte[] serialized = byteOut.toByteArray();
      ByteBuffer buffer = ByteBuffer.allocate(4 + serialized.length);
      buffer.putInt(serialized.length);
      buffer.put(serialized);
      buffer.flip();
      channel.write(buffer);

      ByteBuffer lenBuffer = ByteBuffer.allocate(4);
      while (lenBuffer.hasRemaining()) {
        channel.read(lenBuffer);
      }
      lenBuffer.flip();
      int len = lenBuffer.getInt();

      ByteBuffer dataBuffer = ByteBuffer.allocate(len);
      while (dataBuffer.hasRemaining()) {
        channel.read(dataBuffer);
      }

      dataBuffer.flip();
      byte[] responseBytes = new byte[len];
      dataBuffer.get(responseBytes);

      ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(responseBytes));
      Object obj = ois.readObject();

      assertTrue(obj instanceof Response);
      assertTrue(((Response) obj).getMessage().toLowerCase().contains("help"));
    }
  }

  @Test
  public void testShutdownMethod() throws Exception {
    Selector selector = Selector.open();
    ServerSocketChannel serverSocket = ServerSocketChannel.open();
    serverSocket.bind(new InetSocketAddress(0));
    serverSocket.configureBlocking(false);
    serverSocket.register(selector, SelectionKey.OP_ACCEPT);

    assertTrue(serverSocket.isOpen());
    assertTrue(selector.isOpen());

    var method =
        ServerInstance.class.getDeclaredMethod(
            "shutdown", Selector.class, ServerSocketChannel.class);
    method.setAccessible(true);
    method.invoke(serverInstance, selector, serverSocket);

    assertFalse(serverSocket.isOpen());
    assertFalse(selector.isOpen());
  }
}
