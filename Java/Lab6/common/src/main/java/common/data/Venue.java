package common.data;

import static common.managers.IdManager.venueIdGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Класс отвечающий за поле Venue в структуре Ticket
 *
 * @author Maks
 * @version 1.0
 */
public class Venue implements Serializable {
  private static final long serialVersionUID = -2903231859822864341L;

  @Min(value = 1, message = "Значение поля должно быть больше 0")
  @NotNull(message = "Поле id не может быть null")
  private Integer
      id; // Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно

  // быть уникальным, Значение этого поля должно генерироваться автоматически

  @NotNull(message = "Поле id не может быть 0")
  private String name; // Поле не может быть null, Строка не может быть пустой

  @Min(value = 0, message = "Значение поля capacity должно быть больше 0")
  private int capacity; // Значение поля должно быть больше 0

  @NotNull(message = "Поле type не может быть null")
  private VenueType type; // Поле может быть null

  @NotNull(message = "Поле address не может быть null")
  private Address address; // Поле не может быть null

  public Venue(
      @JsonProperty("name") String name,
      @JsonProperty("capacity") int capacity,
      @JsonProperty("type") VenueType type,
      @JsonProperty("address") Address address) {
    this.id = venueIdGenerator.generateId();
    this.name = name;
    this.capacity = capacity;
    this.type = type;
    this.address = address;
  }

  @Override
  public String toString() {
    return "Venue{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", capacity="
        + capacity
        + ", type="
        + type
        + ", address="
        + address
        + '}';
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public VenueType getType() {
    return type;
  }

  public void setType(VenueType type) {
    this.type = type;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Venue venue = (Venue) obj;
    return capacity == venue.capacity
        && Objects.equals(name, venue.name)
        && type == venue.type
        && Objects.equals(address, venue.address);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, capacity, type, address);
  }
}
