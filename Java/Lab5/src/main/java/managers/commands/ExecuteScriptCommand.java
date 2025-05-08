package managers.commands;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import managers.CommandManager;
import system.InputManager;
import system.TextColor;

/**
 * Класс команды, которая читает скрипт из файла и выполняет его. Защищён от рекурсии.
 *
 * @author Maks
 * @version 1.0
 */
public class ExecuteScriptCommand implements Command {
  private final Set<String> runningScripts = new HashSet<>();
  private CommandManager commandManager;

  public ExecuteScriptCommand() {}

  public void setCommandManager(CommandManager commandManager) {
    this.commandManager = commandManager;
  }

  @Override
  public void execute(String[] args) {
    if (args.length == 0) {
      TextColor.errorMessage("Укажите путь к файлу со скриптом");
      return;
    }

    String scriptPath = args[0];
    Path path = Paths.get(scriptPath).toAbsolutePath();

    if (runningScripts.contains(path.toString())) {
      System.out.println("Обнаружена рекурсия, скрипт " + path + " уже выполняется");
      return;
    }

    if (!Files.exists(path) || !Files.isReadable(path)) {
      TextColor.errorMessage("Файл не найден или недоступен");
      return;
    }

    runningScripts.add(path.toString());

    try (InputStream inputStream = Files.newInputStream(path)) {
      Scanner previousScanner = InputManager.getScanner();
      InputManager.setInput(inputStream);

      while (InputManager.hasNextLine()) {
        String line = InputManager.nextLine().trim();
        if (line.isEmpty() || line.startsWith("#")) continue;
        System.out.println(">>> " + line);
        commandManager.executeCommand(line);
      }

      InputManager.setScanner(previousScanner);
    } catch (IOException e) {
      TextColor.errorMessage("Ошибка при чтении файла: ");
      System.out.println(e.getMessage());
    } finally {
      runningScripts.remove(path.toString());
    }
  }

  @Override
  public String getDescription() {
    return "исполняет скрипт из файла";
  }
}
