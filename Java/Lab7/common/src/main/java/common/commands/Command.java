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
public abstract class Command {
  private final String name;
  private final boolean requireAuth;

  public Command(String name) {
    this.name = name;
    this.requireAuth = true;
  }

  public Command(String name, boolean requireAuth) {
    this.name = name;
    this.requireAuth = requireAuth;
  }

  public String getName() {
    return name;
  }

  public boolean requiresAuth() {
    return requireAuth;
  }

  public RequestBody packageBody(String[] args, Scanner in) throws CommandArgumentException {
    if (args.length != 0) {
      throw new CommandArgumentException(this.getName(), args.length);
    }
    return new RequestBody(args);
  }

  public abstract Response execute(Request request);

  public abstract String getDescription();
}
