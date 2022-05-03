package todo.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import todo.api.controller.ApiResult;

@Slf4j
@RestControllerAdvice
public class TodoExceptionHandler {

  @ExceptionHandler({UserNotFoundException.class, TodoAlreadyExistException.class})
  public final ApiResult<?> userNotFoundException(Exception ex) {
    return ApiResult.ERROR(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(TodoNotFoundException.class)
  public final ApiResult<?> todoNotFoundException(Exception ex) {
    return ApiResult.ERROR(ex.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public final ApiResult<?> notFoundException(NoHandlerFoundException ex) {
    return ApiResult.ERROR(ex.getRequestURL() + " 주소가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public final ApiResult<?> otherException(Exception ex) {
    log.info("error = {}", ex);
    return ApiResult.ERROR(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
