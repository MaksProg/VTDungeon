package common.data;

import java.io.Serializable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Класс отвечающий за поле Coordinates в структуре Ticket
 *
 * @author Maks
 * @version 1.0
 */
public class Coordinates implements Serializable {
  private static final long serialVersionUID = -2903231858822863341L;

  @NotNull(message = "Поле x не может быть null")
  private Double x; // Поле не может быть null

  @NotNull(message = "Поле x не может быть null")
  @Min(value = -592, message = "Поле y должно быть больше -593")
  private Integer y; // Значение поля должно быть больше -593, Поле не может быть null

  public Coordinates() {}

  public Coordinates(Double x, Integer y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return "Coordinates{" + "x=" + x + ", y=" + y + '}';
  }

  public Double getX() {
    return x;
  }

  public void setX(Double x) {
    this.x = x;
  }

  public Integer getY() {
    return y;
  }

  public void setY(Integer y) {
    this.y = y;
  }
}
