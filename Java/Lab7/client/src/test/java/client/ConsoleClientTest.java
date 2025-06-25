package client;

import static org.junit.jupiter.api.Assertions.*;

import common.managers.ClientCommandManager;
import common.managers.CommandManager;
import common.network.ObjectEncoder;
import common.network.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import org.junit.jupiter.api.*;

class ConsoleClientTest {
  private ConsoleClient client;
  private FakeChannelWrapper fakeChannel;

  @BeforeEach
  void setup() {
    CommandManager commandManager = new ClientCommandManager(new EmptyCollectionManager());
    client = new ConsoleClient(new InetSocketAddress("localhost", 12345), commandManager);

    fakeChannel = new FakeChannelWrapper();
    try {
      var field = ConsoleClient.class.getDeclaredField("channelWrapper");
      field.setAccessible(true);
      field.set(client, fakeChannel);
    } catch (ReflectiveOperationException e) {
      fail("Не удалось установить fake channelWrapper", e);
    }
  }

  @Test
  void testRunConnectsToServerAndStartsLoop() throws IOException, InterruptedException {
    int port = 12345;
    SocketAddress address = new InetSocketAddress("localhost", port);

    Thread serverThread =
        new Thread(
            () -> {
              try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
                serverSocket.socket().bind(address);
                SocketChannel clientSocket = serverSocket.accept();

                ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
                clientSocket.read(sizeBuffer);
                sizeBuffer.flip();
                int size = sizeBuffer.getInt();

                ByteBuffer dataBuffer = ByteBuffer.allocate(size);
                clientSocket.read(dataBuffer);

                Response response = new Response("Тестовый ответ с сервера");
                ByteBuffer responseBuffer = ObjectEncoder.encodeObject(response);
                responseBuffer.flip();
                clientSocket.write(responseBuffer);
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
    serverThread.start();

    DummyCommandManager manager = new DummyCommandManager();
    ConsoleClient client = new ConsoleClient(address, manager);

    InputStream testInput = new ByteArrayInputStream("history\n".getBytes(StandardCharsets.UTF_8));
    System.setIn(testInput); // Перехватываем ввод

    Thread clientThread = new Thread(client::run);
    clientThread.start();

    Thread.sleep(2000);
    client.stopClient();
    clientThread.join();
  }

  @Test
  void testInputLoop_withHistoryCommand() throws Exception {
    int port = 56789;
    InetSocketAddress address = new InetSocketAddress("localhost", port);

    Thread serverThread =
        new Thread(
            () -> {
              try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
                serverSocket.bind(address);
                try (SocketChannel clientSocket = serverSocket.accept()) {

                  ByteBuffer sizeBuf = ByteBuffer.allocate(4);
                  clientSocket.read(sizeBuf);
                  sizeBuf.flip();
                  int size = sizeBuf.getInt();

                  ByteBuffer objBuf = ByteBuffer.allocate(size);
                  clientSocket.read(objBuf);
                  objBuf.flip();

                  Response response = new Response("История выполнена");
                  ByteBuffer responseBuffer = ObjectEncoder.encodeObject(response);
                  responseBuffer.flip();
                  clientSocket.write(responseBuffer);
                }
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
    serverThread.start();

    String command = "history\n";
    InputStream input = new ByteArrayInputStream(command.getBytes(StandardCharsets.UTF_8));
    System.setIn(input);

    ConsoleClient client = new ConsoleClient(address, new DummyCommandManager());
    Thread clientThread = new Thread(client::run);
    clientThread.start();

    Thread.sleep(3000);
    client.stopClient();
    clientThread.join();
  }

  @Test
  void testExecuteScript_basic() throws IOException {

    Path tempScript = Files.createTempFile("test_script", ".txt");
    Files.writeString(tempScript, "help\nexit\n");

    fakeChannel.addMockResponse(new Response("Команда help выполнена"));
    fakeChannel.addMockResponse(new Response("Выход из клиента"));

    client.stopClient();
    client.executeScript(tempScript.toString());

    assertEquals(2, fakeChannel.getSentMessages().size());
  }
}
