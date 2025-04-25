package managers.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import system.ApplicationContext;
import system.InputManager;

/**
 * Класс команды, которая читает скрипт из файла и выполняет его. Защищён от рекурсии.
 *
 * @author Maks
 * @version 1.0
 */
public class ExecuteScriptCommand implements Command {
  private static final Set<String> runningScripts = new HashSet<>();

  @Override
  public void execute(String[] args) {
    if (args.length == 0) {
      System.out.println("Укажите путь к файлу со скриптом");
      return;
    }

    String scriptPath = args[0];
    Path path = Paths.get(scriptPath).toAbsolutePath();

    if (runningScripts.contains(path.toString())) {
      System.out.println("Обнаружена рекурсия, скрипт " + path + " уже выполняется");
      return;
    }

    if (!Files.exists(path) || !Files.isReadable(path)) {
      System.out.println("Файл не найден или недоступен");
      return;
    }

    runningScripts.add(path.toString());

    try (Scanner fileScanner = new Scanner(Files.newInputStream(path))) {
      Scanner previousScanner = InputManager.getScanner();

      InputManager.setScanner(fileScanner);

      while (fileScanner.hasNextLine()) {
        String line = fileScanner.nextLine().trim();
        if (line.isEmpty() || line.startsWith("#")) continue;

        System.out.println(">>> " + line);

        ApplicationContext.getCommandManager().executeCommand(line);
      }

      InputManager.setScanner(previousScanner);

    } catch (IOException e) {
      System.out.println("Ошибка при чтении файла: " + e.getMessage());
    } finally {
      runningScripts.remove(path.toString());
    }
  }
}
