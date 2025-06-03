package server.collectionManager;

import common.data.Ticket;
import common.system.JSONReader;
import common.system.JSONWriter;
import common.system.utils.TicketLocationComparator;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

/**
 * Менеджер коллекции {@link Ticket}, хранящейся в файле в формате JSON.
 *
 * <p>Отвечает за загрузку, сохранение и управление коллекцией билетов. При создании загружает
 * коллекцию из файла, если он существует и не пуст, иначе создаёт пустую коллекцию.
 *
 * <p>Поддерживает операции:
 *
 * <ul>
 *   <li>Получение даты инициализации коллекции.
 *   <li>Получение и замена коллекции билетов.
 *   <li>Добавление и удаление билетов по идентификатору.
 *   <li>Поиск максимального билета (сравнение через {@link Ticket#compareTo}).
 *   <li>Сохранение коллекции обратно в файл.
 * </ul>
 */
public class FileCollectionManager implements SaveableCollectionManager {
  private Deque<Ticket> ticketCollection;
  private final ZonedDateTime initDate;
  private final String filePath;
  private final JSONReader reader;

  /**
   * Создаёт менеджер коллекции, загружая данные из файла.
   *
   * @param filePath путь к JSON-файлу с коллекцией
   * @param reader объект для чтения JSON
   * @throws IOException если произошла ошибка при чтении файла
   */
  public FileCollectionManager(String filePath, JSONReader reader) throws IOException {
    this.initDate = ZonedDateTime.now();
    this.filePath = filePath;
    this.reader = reader;

    File file = new File(filePath);
    if (!file.exists() || file.length() == 0) {
      this.ticketCollection = new ArrayDeque<>();
      System.out.println("Файл не найден или пуст. Создана пустая коллекция.");
    } else {
      this.ticketCollection = reader.readTicketsFromJson(filePath);
      sortCollectionByLocation();
      System.out.println("Коллекция успешно загружена из файла.");
    }
  }

  /**
   * Возвращает дату инициализации менеджера коллекции.
   *
   * @return дата и время создания коллекции
   */
  @Override
  public ZonedDateTime getInitDate() {
    return initDate;
  }

  /**
   * Возвращает коллекцию билетов в виде {@link Deque}.
   *
   * @return коллекция билетов
   */
  @Override
  public Deque<Ticket> getDequeCollection() {
    return ticketCollection;
  }

  /** Очищает коллекцию билетов. */
  public void clearCollection() {
    ticketCollection.clear();
  }

  /**
   * Заменяет текущую коллекцию новой.
   *
   * @param newCollection новая коллекция билетов
   */
  public void setDequeCollection(Deque<Ticket> newCollection) {
    ticketCollection = newCollection;
    sortCollectionByLocation();
  }

  /**
   * Добавляет билет в коллекцию.
   *
   * @param ticket билет для добавления
   */
  public void addTicket(Ticket ticket) {
    ticketCollection.add(ticket);
    sortCollectionByLocation();
  }

  /**
   * Удаляет билет из коллекции по идентификатору.
   *
   * @param id идентификатор билета
   * @return {@code true}, если билет был найден и удалён, иначе {@code false}
   */
  public boolean removeById(int id) {
    boolean removed = ticketCollection.removeIf(ticket -> ticket.getId() == id);
    if (removed) sortCollectionByLocation(); // здесь
    return removed;
  }

  /**
   * Возвращает билет с максимальным значением (сравнение по {@link Ticket#compareTo}).
   *
   * @return максимальный билет или {@code null}, если коллекция пуста
   */
  @Override
  public Ticket getMaxTicket() {
    return ticketCollection.stream().max(Ticket::compareTo).orElse(null);
  }

  /** Сохраняет коллекцию билетов в файл в формате JSON. */
  public void save() {
    JSONWriter writer = new JSONWriter(reader.getMapper());
    writer.writeTicketsToJson(ticketCollection, filePath);
    System.out.println("Коллекция успешно сохранена в файл.");
  }

  /** Сортирует коллекцию по Coordinates*/
  private void sortCollectionByLocation() {
    ArrayList<Ticket> sortedList = new ArrayList<>(ticketCollection);
    sortedList.sort(new TicketLocationComparator());
    ticketCollection = new ArrayDeque<>(sortedList);
  }

}
