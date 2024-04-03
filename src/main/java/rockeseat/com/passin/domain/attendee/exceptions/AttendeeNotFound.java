package rockeseat.com.passin.domain.attendee.exceptions;

public class AttendeeNotFound extends RuntimeException{

    public AttendeeNotFound(String message){
        super(message);
    }
}
