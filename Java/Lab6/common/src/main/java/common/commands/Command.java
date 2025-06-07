package common.commands;

import common.exceptions.CommandArgumentException;
import common.network.Request;
import common.network.RequestBody;
import common.network.Response;
import java.util.Scanner;

/**
 * Интерфейс для реализации команд
 *
 * @author Maks
 * @version 1.0
 */
public interface Command {
  Response execute(Request request);

  RequestBody packageBody(String[] args, Scanner in) throws CommandArgumentException;

  String getDescription();
}
