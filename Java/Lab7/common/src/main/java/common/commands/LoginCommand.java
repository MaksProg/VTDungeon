package common.commands;

import common.data.auth.AuthCredentials;
import common.exceptions.CommandArgumentException;
import common.managers.auth.UserManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.Response;
import common.network.ResponseWithAuthCredentials;
import java.util.Scanner;

public class LoginCommand extends Command {
  private UserManager users;

  public LoginCommand(UserManager users) {
    super("login", false);
    this.users = users;
  }

  public LoginCommand() {
    super("login");
  }

  @Override
  public RequestBody packageBody(String[] args, Scanner in) throws CommandArgumentException {
    if (args.length > 0) {
      throw new CommandArgumentException(this.getName(), args.length);
    }
    System.out.println("Введите логин");
    String login = in.nextLine();
    System.out.println("Введите пароль");
    String password = String.valueOf(System.console().readPassword());

    return new RequestBody(new String[] {login, password});
  }

  @Override
  public Response execute(Request request) {
    AuthCredentials newCredentials =
        new AuthCredentials(request.getRequestBody().getArg(0), request.getRequestBody().getArg(1));

    Integer currentUserId = users.authenticate(newCredentials);

    if (currentUserId == null) {
      return new Response("Логин или пароль введены неверно", new Object[] {});
    }

    return new ResponseWithAuthCredentials(
        newCredentials, "Вы успешно вошли в систему, ваш Id:" + currentUserId);
  }

  @Override
  public String getDescription() {
    return "осуществляет вход в систему";
  }
}
