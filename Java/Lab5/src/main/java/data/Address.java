package data;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

/**
 * Класс отвечающий за поле Address в структуре Ticket
 *
 * @author Maks
 * @version 1.0
 */
public class Address {
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
  public String toString() {
    return "Address{" + "Town:" + town + ", zipCode=" + zipCode + "}";
  }
}
