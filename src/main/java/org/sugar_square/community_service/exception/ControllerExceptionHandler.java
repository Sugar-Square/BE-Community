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

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler
  public ErrorResponse handleAnyException(Exception e) {
    return new ErrorResponse("server internal exception", e.getMessage());
  }

  public record ErrorResponse(
      String code,
      String message
  ) {

  }
}
