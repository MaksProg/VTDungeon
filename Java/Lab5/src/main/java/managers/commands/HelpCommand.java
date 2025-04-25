package managers.commands;

/**
 * Класс команды которая выводит список всех команд
 *
 * @author Maks
 * @version 1.0
 */
public class HelpCommand implements Command {
  @Override
  public void execute(String[] args) {
    System.out.println("Доступные команды:");
    System.out.println("help : вывести справку по доступным командам");
    System.out.println("info : вывести в стандартный поток вывода информацию о коллекции");
    System.out.println("show : вывести в стандартный поток вывода все элементы коллекции");
    System.out.println("add {element} : добавить новый элемент в коллекцию");
    System.out.println(
        "update id {element} : обновить значение элемента коллекции, id которого равен заданному");
    System.out.println("remove_by_id id : удалить элемент из коллекции по его id");
    System.out.println("clear : очистить коллекцию");
    System.out.println("save : сохранить коллекцию в файл");
    System.out.println("execute_script file_name : считать и исполнить скрипт из указанного файла");
    System.out.println("exit : завершить программу (без сохранения в файл)");
    System.out.println(
        "add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента(сравнение по вместительности площадки)");
    System.out.println(
        "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный(сравнение по цене билета)");
    System.out.println("history : вывести последние 6 команд");
    System.out.println(
        "remove_any_by_venue venue : удалить из коллекции один элемент по полю venue");
    System.out.println(
        "sum_of_price : вывести сумму значений поля price для всех элементов коллекции");
    System.out.println(
        "count_by_venue venue : вывести количество элементов, значение поля venue которых равно заданному");
  }
}
