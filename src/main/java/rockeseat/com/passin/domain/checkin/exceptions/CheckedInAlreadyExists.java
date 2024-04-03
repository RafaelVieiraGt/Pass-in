package rockeseat.com.passin.domain.checkin.exceptions;

public class CheckedInAlreadyExists extends RuntimeException{

    public CheckedInAlreadyExists(String message){
        super(message);
    }
}
