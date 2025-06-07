package common.data;

import java.io.Serializable;

/**
 * Перечисление отвечающее за тип площадки в структуре Ticket
 *
 * @author Maks
 * @version 1.0
 */
public enum VenueType implements Serializable {
  PUB("PUB"),
  BAR("BAR"),
  LOFT("LOFT"),
  CINEMA("CINEMA"),
  STADIUM("STADIUM");

  private final String description;

  VenueType(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return this.description;
  }
}
