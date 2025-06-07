package common.data.generators;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Класс отвечающий за генерацию поля ID в Ticket
 *
 * @author Maks
 * @version 1.0
 */
public class IdGenerator {
  private final AtomicInteger counter = new AtomicInteger(1);
  private final Set<Integer> usedIds = new HashSet<>();

  public int generateId() {
    while (true) {
      int id = counter.getAndIncrement();
      synchronized (usedIds) {
        if (!usedIds.contains(id)) {
          usedIds.add(id);
          return id;
        }
      }
    }
  }

  public void registerId(int id) {
    usedIds.add(id);
    counter.updateAndGet(current -> Math.max(current, id + 1));
  }

  public void reset() {
    usedIds.clear();
    counter.set(1);
  }
}
