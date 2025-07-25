package data;

/**
 * Перечисление отвечающее за тип площадки в структуре Ticket
 *
 * @author Maks
 * @version 1.0
 */
public enum VenueType {
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
