package common.system;

import static common.managers.IdManager.ticketIdGenerator;
import static common.managers.IdManager.venueIdGenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.data.Ticket;
import common.exceptions.InvalidJsonFileException;
import common.managers.VenueManager;
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
  private final VenueManager venueManager;
  private final ObjectMapper objectMapper;
  private final Validator validator;

  public JSONReader(ObjectMapper objectMapper, VenueManager venueManager) {
    this.objectMapper = objectMapper;
    this.venueManager = venueManager;

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
        ticketIdGenerator.registerId(ticket.getId());
        if (ticket.getVenue() != null) {
          venueManager.addVenue(ticket.getVenue());
          venueIdGenerator.registerId(ticket.getVenue().getId());
        }
      }

      return new ArrayDeque<>(Arrays.asList(tickets));
    } catch (IOException e) {
      throw new InvalidJsonFileException("Ошибка при чтении JSON-файла: " + filename, e);
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

  public ObjectMapper getMapper() {
    return objectMapper;
  }
}
