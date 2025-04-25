package system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import data.Ticket;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Класс для чтения JSON
 *
 * @author Maks
 * @version 1.0
 */
public class JSONReader {

  private final ObjectMapper objectMapper;
  private final Validator validator;

  public JSONReader() {
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule());

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    this.validator = factory.getValidator();
  }

  /**
   * Функция для чтения билетов из файла
   *
   * @param filename название файла
   * @return Возвращает коллекцию
   */
  public ArrayDeque<Ticket> readTicketsFromJson(String filename) {
    try (InputStreamReader reader = new InputStreamReader(new FileInputStream(filename))) {
      Ticket[] tickets = objectMapper.readValue(reader, Ticket[].class);

      for (Ticket ticket : tickets) {
        validateTicket(ticket);
      }

      return new ArrayDeque<>(Arrays.asList(tickets));
    } catch (IOException e) {
      throw new RuntimeException(
          TextColor.ANSI_RED + "Ошибка при чтении файла:" + e.getMessage() + TextColor.ANSI_RESET,
          e);
    }
  }

  /**
   * Валидирует билеты
   *
   * @param ticket билет
   */
  private void validateTicket(Ticket ticket) {
    Set<ConstraintViolation<Ticket>> violations = validator.validate(ticket);

    if (!violations.isEmpty()) {
      StringBuilder errorMessage = new StringBuilder("Ошибки валидации для Ticket:\n");
      for (ConstraintViolation<Ticket> violation : violations) {
        errorMessage.append("- ").append(violation.getMessage()).append("\n");
      }
      throw new IllegalArgumentException(errorMessage.toString());
    }
  }
}
