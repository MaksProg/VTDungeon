package system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import data.Ticket;
import exceptions.InvalidJsonFileException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Deque;

/**
 * Класс отвечающий за запись билетов в файл
 *
 * @author Maks
 * @version 1.0
 */
public class JSONWriter {

  private final ObjectMapper objectMapper;

  public JSONWriter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    this.objectMapper.registerModule(new JavaTimeModule());
    this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
  }

  public void writeTicketsToJson(Deque<Ticket> tickets, String filename) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
      String jsonString = objectMapper.writeValueAsString(tickets);
      writer.print(jsonString);
    } catch (IOException e) {
      throw new InvalidJsonFileException("Ошибка при записи JSON-файла: " + filename, e);
    }
  }
}
