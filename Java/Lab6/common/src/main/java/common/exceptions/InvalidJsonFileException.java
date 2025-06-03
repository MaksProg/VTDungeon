package common.exceptions;

public class InvalidJsonFileException extends RuntimeException {
  public InvalidJsonFileException(String message, Throwable cause) {
    super(message, cause);
  }
}
