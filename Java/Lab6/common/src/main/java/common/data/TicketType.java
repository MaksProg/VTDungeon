package common.data;

import java.io.Serializable;

/**
 * Перечисление отвечающее за тип билета в структуре Ticket
 *
 * @author Maks
 * @version 1.0
 */
public enum TicketType implements Serializable {
  VIP("VIP"),
  USUAL("USUAL"),
  BUDGETARY("BUDGETARY"),
  CHEAP("CHEAP");

  private final String description;

  TicketType(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return this.description;
  }
}
