package system;

import java.io.InputStream;
import java.util.Scanner;

public class InputManager {
  private static Scanner scanner = new Scanner(System.in);

  public static void setScanner(Scanner newScanner) {
    scanner = newScanner;
  }

  public static Scanner getScanner() {
    return scanner;
  }

  public static String nextLine() {
    return scanner.nextLine();
  }

  public static boolean hasNextLine() {
    return scanner.hasNextLine();
  }

  public static void setInput(InputStream inputStream) {
    scanner = new Scanner(inputStream);
  }
}
