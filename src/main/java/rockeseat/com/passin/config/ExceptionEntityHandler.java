package rockeseat.com.passin.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import rockeseat.com.passin.domain.attendee.exceptions.AttendeeAlreadyRegistered;
import rockeseat.com.passin.domain.attendee.exceptions.AttendeeNotFound;
import rockeseat.com.passin.domain.checkin.exceptions.CheckedInAlreadyExists;
import rockeseat.com.passin.domain.event.exceptions.EventFull;
import rockeseat.com.passin.domain.event.exceptions.EventNotFound;
import rockeseat.com.passin.dto.general.ErrorResponseDto;

@ControllerAdvice
public class ExceptionEntityHandler {
    @ExceptionHandler(EventNotFound.class)
    public ResponseEntity handleEventNotFound(EventNotFound exception){

        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AttendeeNotFound.class)
    public ResponseEntity handleAttendeeNotFound(AttendeeNotFound exception){

        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AttendeeAlreadyRegistered.class)
    public ResponseEntity handleAttendeeAlreadyRegistered(AttendeeAlreadyRegistered exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(CheckedInAlreadyExists.class)
    public ResponseEntity handleCheckInAlreadyExists(CheckedInAlreadyExists exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(EventFull.class)
    public ResponseEntity<ErrorResponseDto> handleEventFull(EventFull exception){
        return ResponseEntity.badRequest().body(new ErrorResponseDto(exception.getMessage()));
    }
}
