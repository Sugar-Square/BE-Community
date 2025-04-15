package org.sugar_square.community_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class ControllerExceptionHandler {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(EntityNotFoundException.class)
  public ErrorResponse handleEntityNotFoundException(EntityNotFoundException e) {
    return new ErrorResponse("entity not found", e.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException.class)
  public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
    return new ErrorResponse("invalid request", e.getMessage());
  }

  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(AccessDeniedException.class)
  public ErrorResponse handleAccessDeniedException(AccessDeniedException e) {
    return new ErrorResponse("access denied", e.getMessage());
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public ErrorResponse handleAnyException(Exception e) {
    return new ErrorResponse("server internal exception", "서버 내부 에러가 발생했습니다.");
  }

  public record ErrorResponse(
      String code,
      String message
  ) {

  }
}
