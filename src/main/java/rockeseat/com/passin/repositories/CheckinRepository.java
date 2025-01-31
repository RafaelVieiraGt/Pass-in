package rockeseat.com.passin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rockeseat.com.passin.domain.checkin.Checkin;

import java.util.Optional;

public interface CheckinRepository extends JpaRepository<Checkin, Integer> {
    Optional<Checkin> findByAttendeeId(String attendeeId);
}
