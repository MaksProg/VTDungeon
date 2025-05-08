package managers;

import data.Ticket;
import java.time.ZonedDateTime;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Класс, который управляет коллекцией
 *
 * @author Maks
 * @version 1.0
 */
public class CollectionManager {
  private Deque<Ticket> dequeCollection;
  private ZonedDateTime initDate;

  public CollectionManager(ZonedDateTime initDate) {
    this.dequeCollection = new ArrayDeque<>();
    this.initDate = initDate;
  }

  public ZonedDateTime getInitDate() {
    return initDate;
  }

  public Deque<Ticket> getDequeCollection() {
    return dequeCollection;
  }

  /**
   * Функция очищающая коллекцию
   *
   * @author Maks
   */
  public void clearCollection() {
    dequeCollection.clear();
  }

  /**
   * Функция для хранения коллекции из файла
   *
   * @param newCollection Коллекция экземпляров класса Ticket
   */
  public void setDequeCollection(ArrayDeque<Ticket> newCollection) {
    dequeCollection = newCollection;
  }

  /**
   * Функция добавляющая новый билет в коллекцию
   *
   * @param ticket Новый билет
   */
  public void addTicket(Ticket ticket) {
    dequeCollection.add(ticket);
  }

  /**
   * Функция удаляющая билет по id
   *
   * @param id id билета
   * @return Возвращает коллекцию без билета с id который мы передаём
   */
  public boolean removeById(int id) {
    return dequeCollection.removeIf(ticket -> ticket.getId() == id);
  }

  public Ticket getMaxTicket() {
    return dequeCollection.stream().max(Ticket::compareTo).orElse(null);
  }
}
