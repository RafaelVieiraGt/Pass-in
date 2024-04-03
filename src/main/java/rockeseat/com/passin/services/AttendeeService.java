package rockeseat.com.passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import rockeseat.com.passin.domain.attendee.Attendee;
import rockeseat.com.passin.domain.attendee.exceptions.AttendeeAlreadyRegistered;
import rockeseat.com.passin.domain.attendee.exceptions.AttendeeNotFound;
import rockeseat.com.passin.domain.checkin.Checkin;
import rockeseat.com.passin.dto.attendee.AttendeeBadgeResponseDto;
import rockeseat.com.passin.dto.attendee.AttendeeDetail;
import rockeseat.com.passin.dto.attendee.AttendeesListResponseDto;
import rockeseat.com.passin.dto.attendee.AttendeeBadgeDto;
import rockeseat.com.passin.repositories.AttendeeRepository;
import rockeseat.com.passin.repositories.CheckinRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final CheckinRepository checkinRepository;
    private final CheckInService checkInService;

    public List<Attendee> getAllAttendeesFromEvent(String eventId){
        List<Attendee> attendeeList = this.attendeeRepository.findByEventId(eventId);

        return attendeeList;
    }
    public AttendeesListResponseDto getEventsAttendee(String eventId){
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);

        List<AttendeeDetail> attendeeDetailList = attendeeList.stream().map(attendee -> {
            Optional<Checkin> checkIn = this.checkinRepository.findByAttendeeId(attendee.getId());
            LocalDateTime checkedInAt = checkIn.isPresent() ? checkIn.get().getCreatedAt() : null;
            return new AttendeeDetail(attendee.getId(),attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkedInAt);
        }).toList();

        return new AttendeesListResponseDto(attendeeDetailList);

    }


    public Attendee registerAttendee(Attendee newAttendee){
        this.attendeeRepository.save(newAttendee);

        return newAttendee;
    }

    public void verifyAttendeeSubscription(String email, String eventId){
        Optional<Attendee> isAttendeeRegistered = this.attendeeRepository.findByEventIdAndEmail(eventId, email);

        if(isAttendeeRegistered.isPresent()){
            throw new AttendeeAlreadyRegistered("Attendee is already registered");
        }
    }

    public AttendeeBadgeResponseDto getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder){
        Attendee attendee = this.getAttendee(attendeeId);
        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/check-in").buildAndExpand(attendeeId).toUri().toString();

        AttendeeBadgeDto attendeeBadgeDto = new AttendeeBadgeDto(attendee.getName(), attendee.getEmail(), uri, attendee.getEvent().getId());
        return new AttendeeBadgeResponseDto(attendeeBadgeDto);
    }

    public void checkInAttendee(String attendeeId){
        Attendee attendee = this.getAttendee(attendeeId);
        this.checkInService.checkInAttendee(attendee);
    }

    private Attendee getAttendee(String attendeeId){
        return this.attendeeRepository.findById(attendeeId).orElseThrow(() -> new AttendeeNotFound("Attendee not found"));
    }
}
