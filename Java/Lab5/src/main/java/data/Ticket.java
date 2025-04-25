package data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import data.generators.TicketIdGenerator;
import java.time.ZonedDateTime;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Класс имитирующий структуру билета "Ticket"
 *
 * @author Maks
 * @version 1.0
 */
public class Ticket {
  @JsonIgnore
  private int id; // Значение поля должно быть больше 0, Значение этого поля должно быть уникальным,

  // Значение этого поля должно генерироваться автоматически

  @NotNull(message = "Поле name не может быть null")
  @NotEmpty(message = "Поле name не может быть пустым")
  private String name; // Поле не может быть null, Строка не может быть пустой

  @NotNull(message = "Поле coordinates не может быть null")
  @Valid
  private Coordinates coordinates; // Поле не может быть null

  @NotNull(message = "Поле creationDate не может быть null")
  private java.time.ZonedDateTime
      creationDate; // Поле не может быть null, Значение этого поля должно генерироваться

  // автоматически

  @NotNull(message = "Поле price не может быть null")
  @Min(value = 0, message = "Значение поля price должно быть больше 0")
  private Double price; // Поле не может быть null, Значение поля должно быть больше 0

  @NotNull(message = "Поле type не может быть null")
  @Valid
  private TicketType type; // Поле не может быть null

  @Valid
  @NotNull(message = "Поле venue не может быть null")
  private Venue venue; // Поле может быть null

  public Ticket() {
    this.id = TicketIdGenerator.getId();
    this.creationDate = ZonedDateTime.now();
  }

  public Ticket(
      @JsonProperty("name") String name,
      @JsonProperty("coordinates") Coordinates coordinates,
      @JsonProperty("price") Double price,
      @JsonProperty("type") TicketType type,
      @JsonProperty("venue") Venue venue) {
    this.id = TicketIdGenerator.getId();
    this.name = name;
    this.coordinates = coordinates;
    this.creationDate = ZonedDateTime.now();
    this.price = price;
    this.type = type;
    this.venue = venue;
  }

  @Override
  public String toString() {
    return "Ticket{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", coordinates="
        + coordinates
        + ", creationDate="
        + creationDate
        + ", price="
        + price
        + ", type="
        + type
        + ", venue="
        + venue
        + '}';
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Coordinates getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(Coordinates coordinates) {
    this.coordinates = coordinates;
  }

  public ZonedDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(ZonedDateTime creationDate) {
    this.creationDate = creationDate;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public TicketType getType() {
    return type;
  }

  public void setType(TicketType type) {
    this.type = type;
  }

  public Venue getVenue() {
    return venue;
  }

  public void setVenue(Venue venue) {
    this.venue = venue;
  }
}
