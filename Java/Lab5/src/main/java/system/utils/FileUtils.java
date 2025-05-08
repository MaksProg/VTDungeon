package system.utils;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Класс реализующий работу с файлами
 * @author Maks
 * @version 1.0
 */
public class FileUtils {
  /**
   * Функция которая получает время создания файла
   * @param filePath
   * @return Время создания файла, если его невозможно получить, возвращает текущее время
   */
  public static ZonedDateTime getFileCreationDate(String filePath) {
    try {
      Path path = Paths.get(filePath);
      BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
      return attrs.creationTime().toInstant().atZone(ZoneId.systemDefault());
    } catch (IOException e) {
      System.out.println("Не удалось получить дату создания файла, ставлю текущее время.");
      return ZonedDateTime.now();
    }
  }
}
