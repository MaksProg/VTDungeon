package data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * Класс отвечающий за поле Address в структуре Ticket
 *
 * @author Maks
 * @version 1.0
 */
public class Location {
  private long x;

  @NotNull(message = "Поле y не может быть null")
  private Long y; // Поле не может быть null

  private String name; // Поле может быть null

  public Location() {}

  public Location(
      @JsonProperty("x") long x, @JsonProperty("y") Long y, @JsonProperty("name") String name) {
    this.x = x;
    this.y = y;
    this.name = name;
  }

  public long getX() {
    return x;
  }

  public Long getY() {
    return y;
  }

  public String getName() {
    return name;
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Location location = (Location) o;
    return Objects.equals(x, location.x) && Objects.equals(y, location.y);
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public String toString() {
    return "Location{" + "x=" + x + ", y=" + y + ", name=" + name + "}";
  }
}
