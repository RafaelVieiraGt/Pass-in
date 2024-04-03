package rockeseat.com.passin.domain.event.exceptions;

public class EventNotFound extends RuntimeException{

    public EventNotFound (String message){
        super (message);
    }
}
