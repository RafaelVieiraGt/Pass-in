package rockeseat.com.passin.domain.attendee.exceptions;

public class AttendeeAlreadyRegistered extends RuntimeException{

    public AttendeeAlreadyRegistered(String message){
        super(message);
    }
}
