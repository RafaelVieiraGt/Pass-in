package rockeseat.com.passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rockeseat.com.passin.domain.attendee.Attendee;
import rockeseat.com.passin.domain.checkin.Checkin;
import rockeseat.com.passin.domain.checkin.exceptions.CheckedInAlreadyExists;
import rockeseat.com.passin.repositories.CheckinRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckInService {
    private final CheckinRepository checkinRepository;

    public void checkInAttendee(Attendee attendee) {
        this.verifyCheckInExists(attendee.getId());

        Checkin checkin = new Checkin();
        checkin.setAttendee(attendee);
        checkin.setCreatedAt(LocalDateTime.now());
        this.checkinRepository.save(checkin);
    }

    private void verifyCheckInExists(String attendeeId){
        Optional<Checkin> isChecked = this.checkinRepository.findByAttendeeId(attendeeId);
        if(isChecked.isPresent()) throw new CheckedInAlreadyExists("Attendee already checked-in");
    }
}
