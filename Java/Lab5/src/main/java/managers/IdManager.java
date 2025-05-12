package managers;

import data.generators.IdGenerator;

/**
 * Класс хранящий экземпляры генераторов Id для объектов
 *
 * @author Maks
 * @version 1.0
 */
public class IdManager {
  public static final IdGenerator ticketIdGenerator = new IdGenerator();
  public static final IdGenerator venueIdGenerator = new IdGenerator();
}
