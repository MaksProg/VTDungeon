package common.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * Класс отвечающий за поле Address в структуре Ticket
 *
 * @author Maks
 * @version 1.0
 */
public class Address implements Serializable {

  private static final long serialVersionUID = -2923231859822863341L;

  @NotNull(message = "Поле zipCode не может быть null")
  private String zipCode;

  @NotNull(message = "Поле town не может быть null")
  private Location town;

  public Address() {}

  public Address(@JsonProperty("zipCode") String zipCode, @JsonProperty("town") Location town) {
    this.zipCode = zipCode;
    this.town = town;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public Location getTown() {
    return town;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Address address = (Address) o;
    return Objects.equals(zipCode, address.zipCode) && Objects.equals(town, address.town);
  }

  @Override
  public int hashCode() {
    return Objects.hash(zipCode, town);
  }

  @Override
  public String toString() {
    return "Address{" + "Town:" + town + ", zipCode=" + zipCode + "}";
  }
}
