package common.managers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import common.commands.Command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClientCommandManagerTest {

  private CollectionManager collectionManager;
  private ClientCommandManager clientCommandManager;

  @BeforeEach
  public void setUp() {
    collectionManager = mock(CollectionManager.class);
    clientCommandManager = new ClientCommandManager(collectionManager);
  }

  @Test
  public void initCommands_shouldContainExpectedCommands() {

    assertNotNull(clientCommandManager.getCommandList());

    assertTrue(clientCommandManager.getCommandList().containsKey("help"));
    assertTrue(clientCommandManager.getCommandList().containsKey("history"));
    assertTrue(clientCommandManager.getCommandList().containsKey("login"));
    assertTrue(clientCommandManager.getCommandList().containsKey("register"));

    assertNull(clientCommandManager.getCommandList().get("execute_script"));

    assertNotNull(clientCommandManager.getCommandList().get("info"));
    assertNotNull(clientCommandManager.getCommandList().get("show"));
    assertNotNull(clientCommandManager.getCommandList().get("clear"));
    assertNotNull(clientCommandManager.getCommandList().get("add"));
    assertNotNull(clientCommandManager.getCommandList().get("remove_by_id"));
    assertNotNull(clientCommandManager.getCommandList().get("count_by_venue"));
    assertNotNull(clientCommandManager.getCommandList().get("sum_of_price"));
    assertNotNull(clientCommandManager.getCommandList().get("remove_any_by_venue"));
    assertNotNull(clientCommandManager.getCommandList().get("add_if_max"));
    assertNotNull(clientCommandManager.getCommandList().get("remove_greater"));
    assertNotNull(clientCommandManager.getCommandList().get("update"));
    assertNotNull(clientCommandManager.getCommandList().get("add_venue"));
    assertNotNull(clientCommandManager.getCommandList().get("exit"));
  }

  @Test
  public void helpCommand_shouldHaveReferenceToCommandManager() {
    Command helpCommand = clientCommandManager.getCommandList().get("help");
    assertNotNull(helpCommand);
    if (helpCommand instanceof common.commands.HelpCommand help) {
      assertDoesNotThrow(() -> help.setCommandManager(clientCommandManager));
    } else {
      fail("HelpCommand должен быть экземпляром HelpCommand");
    }
  }
}
