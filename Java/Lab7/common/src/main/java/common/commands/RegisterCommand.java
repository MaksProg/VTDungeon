package common.commands;

import common.data.auth.AuthCredentials;
import common.exceptions.CommandArgumentException;
import common.managers.auth.UserManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.Response;
import common.network.ResponseWithAuthCredentials;


import java.util.Scanner;

public class RegisterCommand extends Command{
    private final UserManager users;

    public RegisterCommand(UserManager users){
        super("register", false);
        this.users = users;
    }

     public RegisterCommand(){
         super("register");
         this.users = null;
     }

    @Override
    public RequestBody packageBody(String[] args, Scanner in) throws CommandArgumentException {
        if (args.length > 0) {
            throw new CommandArgumentException(this.getName(), args.length);
        }
        System.out.println("Введите логин:");
        String login = in.nextLine();
        System.out.println("Введите пароль:");
        String password = String.valueOf(System.console().readPassword());

        return new RequestBody(new String[] {login,password});
    }

    @Override
    public Response execute(Request request){
        AuthCredentials newCredentials = new AuthCredentials(
                request.getRequestBody().getArg(0),
                request.getRequestBody().getArg(1)
        );

        Integer newUserId = users.register(newCredentials);

        if (newUserId == null) {
            return new Response("Это имя пользователя уже занято", new Object[] {});
        }

        return new ResponseWithAuthCredentials(newCredentials,"Вы успешно зарегистрированы, ваш id:"+newUserId);
    }

    @Override
    public String getDescription(){
        return "регистрирует пользователя в системе";
    }
}