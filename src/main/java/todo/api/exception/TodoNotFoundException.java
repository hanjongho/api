package todo.api.exception;

public class TodoNotFoundException extends RuntimeException {

  public TodoNotFoundException(String message) {
    super(message);
  }
}
