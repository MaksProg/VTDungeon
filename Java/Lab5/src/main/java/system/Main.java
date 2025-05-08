package system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.ZonedDateTime;
import managers.CollectionManager;
import managers.CommandManager;
import managers.VenueManager;
import managers.commands.ExitCommand;
import system.utils.FileUtils;

public class Main {
  public static void main(String[] args) {
    String dataPath = System.getenv("TICKETS_FILE");
    ZonedDateTime initDate = FileUtils.getFileCreationDate(dataPath);
    VenueManager venueManager = new VenueManager();
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    JSONReader reader = new JSONReader(mapper, venueManager);
    JSONWriter writer = new JSONWriter(mapper);
    CollectionManager collectionManager = new CollectionManager(initDate);
    CommandManager commandManager =
        new CommandManager(collectionManager, venueManager, writer, reader, dataPath);

    ApplicationContext context =
        new ApplicationContext(dataPath, reader, writer, collectionManager, commandManager);

    Console console = new Console(context);
    commandManager.getCommandList().put("exit", new ExitCommand(console));
    console.start(System.in);
  }
}
