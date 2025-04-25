package managers;

import data.Ticket;
import java.time.ZonedDateTime;
import java.util.ArrayDeque;
import system.JSONWriter;

/**
 * Класс который управляет коллекцией
 *
 * @author Maks
 * @version 1.0
 */
public class CollectionManager {
  private static ArrayDeque<Ticket> dequeCollection;
  private static ZonedDateTime initDate;

  public CollectionManager() {
    dequeCollection = new ArrayDeque<>();
    initDate = ZonedDateTime.now();
  }

  public static ZonedDateTime getInitDate() {
    return initDate;
  }

  public static ArrayDeque<Ticket> getDequeCollection() {
    return dequeCollection;
  }

  /**
   * Функция очищающая коллекцию
   *
   * @author Maks
   */
  public static void clearCollection() {
    dequeCollection.clear();
  }

  /**
   * Функция сохраняющая коллекцию в файл
   *
   * @param filename название файла
   * @param jsonWriter класс который организует запись в файл
   */
  public static void saveCollection(String filename, JSONWriter jsonWriter) {
    jsonWriter.writeTicketsToJson(dequeCollection, filename);
  }

  /**
   * Функция для хранения коллекции из файла
   *
   * @param newCollection Коллекция экземпляров класса Ticket
   */
  public static void setDequeCollection(ArrayDeque<Ticket> newCollection) {
    dequeCollection = newCollection;
  }

  /**
   * Функция добавляющая новый билет в коллекцию
   *
   * @param ticket Новый билет
   */
  public static void addTicket(Ticket ticket) {
    dequeCollection.add(ticket);
  }

  /**
   * Функция удаляющая билет по id
   *
   * @param id id билета
   * @return Возвращает коллекцию без билета с id который мы передаём
   */
  public static boolean removeById(int id) {
    return dequeCollection.removeIf(ticket -> ticket.getId() == id);
  }
}
