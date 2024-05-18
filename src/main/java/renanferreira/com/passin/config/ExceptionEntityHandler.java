package renanferreira.com.passin.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import renanferreira.com.passin.domain.attendee.exceptions.AttendeeAlreadyExistsException;
import renanferreira.com.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import renanferreira.com.passin.domain.checkIn.exceptions.CheckInAlreadyExistsExcepetion;
import renanferreira.com.passin.domain.event.exceptions.EventFullException;
import renanferreira.com.passin.domain.event.exceptions.EventNofFoundException;
import renanferreira.com.passin.dto.general.ErrorResponseDTO;

@ControllerAdvice
public class ExceptionEntityHandler {

  @ExceptionHandler(EventNofFoundException.class)
  public ResponseEntity<Object> handlerEventNotFound(EventNofFoundException e) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(EventFullException.class)
  public ResponseEntity<Object> handlerEventFull(EventFullException e) {
    return ResponseEntity.badRequest().body(new ErrorResponseDTO(e.getMessage()));
  }

  @ExceptionHandler(AttendeeNotFoundException.class)
  public ResponseEntity<Object> handlerAttendeeNotFound(AttendeeNotFoundException e) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(AttendeeAlreadyExistsException.class)
  public ResponseEntity<Object> handlerAttendeeAlreadyExists(AttendeeAlreadyExistsException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).build();
  }

  @ExceptionHandler(CheckInAlreadyExistsExcepetion.class)
  public ResponseEntity<Object> handlerCheckInAlreadyExists(CheckInAlreadyExistsExcepetion e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).build();
  }
}
