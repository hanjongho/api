package todo.api.exception;

public class TodoAlreadyExistException extends RuntimeException {

  public TodoAlreadyExistException(String message) {
    super(message);
  }
}
