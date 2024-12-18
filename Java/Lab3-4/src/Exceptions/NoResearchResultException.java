package Exceptions;

public class NoResearchResultException extends RuntimeException {
    public NoResearchResultException(String message) {
        super(message);
    }
    public NoResearchResultException() {
        super(" провёл исследование, но ничего не нашёл"); // Дефолтное сообщение
    }

    @Override
    public String getMessage() {
        return super.getMessage(); // Возвращаем строку, переданную в конструктор
    }
}
