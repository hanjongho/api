package todo.api.controller;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiError {

  private final String message;

  private final int status;

  ApiError(String message, HttpStatus status) {
    this.message = message;
    this.status = status.value();
  }

}