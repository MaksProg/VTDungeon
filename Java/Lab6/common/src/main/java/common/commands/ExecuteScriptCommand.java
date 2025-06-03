package common.commands;

import common.managers.CommandManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.Response;
import common.system.InputManager;
import common.system.utils.TextColor;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ExecuteScriptCommand implements Command {
  private final Set<Path> runningScripts = new HashSet<>();
  private CommandManager commandManager;

  public ExecuteScriptCommand() {}

  public void setCommandManager(CommandManager commandManager) {
    this.commandManager = commandManager;
  }

  @Override
  public Response execute(Request request) {

    throw new UnsupportedOperationException("Команда execute_script выполняется только на клиенте");
  }

  @Override
  public RequestBody packageBody(String[] args, Scanner in) {
    if (args.length != 1) {
      TextColor.errorMessage("Укажите путь к скрипту. Пример: execute_script script.txt");
      return null;
    }

    String path = args[0];
    File file = new File(path).getAbsoluteFile();
    Path path1 = Path.of(file.getPath());

    if (runningScripts.contains(path1)) {
      TextColor.errorMessage(
          "Рекурсивное выполнение обнаружено: файл уже выполняется -> " + file.getPath());
      return null;
    }

    if (!file.exists() || !file.canRead()) {
      TextColor.errorMessage("Файл не найден или не доступен для чтения: " + file.getPath());
      return null;
    }

    Scanner previousScanner = InputManager.getScanner();
    Scanner scriptScanner = null;

    try {
      scriptScanner = new Scanner(file);
      InputManager.setScanner(scriptScanner);
      runningScripts.add(path1);

      while (true) {
        String line = InputManager.safeNextLine();
        if (line == null) break;

        line = line.trim();
        if (line.isEmpty() || line.startsWith("#")) continue;

        System.out.println(">>> " + line);
        commandManager.handleCommandInput(line, InputManager.getScanner());
      }

    } catch (FileNotFoundException e) {
      TextColor.errorMessage("Файл не найден: " + file.getPath());
    } catch (Exception e) {
      TextColor.errorMessage("Ошибка при выполнении скрипта: " + e.getMessage());
    } finally {
      InputManager.setScanner(previousScanner);
      runningScripts.remove(path1);
      if (scriptScanner != null) {
        scriptScanner.close();
      }
    }

    return null;
  }

  @Override
  public String getDescription() {
    return "исполняет скрипт из файла";
  }
}
