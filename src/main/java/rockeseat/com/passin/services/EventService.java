package rockeseat.com.passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rockeseat.com.passin.domain.attendee.Attendee;
import rockeseat.com.passin.domain.event.Event;
import rockeseat.com.passin.domain.event.exceptions.EventFull;
import rockeseat.com.passin.domain.event.exceptions.EventNotFound;
import rockeseat.com.passin.dto.attendee.AttendeeIdDto;
import rockeseat.com.passin.dto.attendee.AttendeeRequestDto;
import rockeseat.com.passin.dto.event.EventIdDto;
import rockeseat.com.passin.dto.event.EventRequestDto;
import rockeseat.com.passin.dto.event.EventResponseDto;
import rockeseat.com.passin.repositories.EventRepository;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final AttendeeService attendeeService;

    public EventResponseDto getEventDetail(String eventId){
        Event evento = this.getEventById(eventId);
        List<Attendee>attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);
        return new EventResponseDto(evento, attendeeList.size());
    }

    public EventIdDto createEvent(EventRequestDto eventRequestDto){
        Event newEvent = new Event();
        newEvent.setTitle(eventRequestDto.title());
        newEvent.setDetails(eventRequestDto.details());
        newEvent.setMaximumAttendees(eventRequestDto.maximumAttendees());
        newEvent.setSlug(createSlug(eventRequestDto.title()));

        this.eventRepository.save(newEvent);
        return new EventIdDto(newEvent.getId());
    }

    private String createSlug(String text){
        String normalize = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalize.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
                .replaceAll("[^\\w\\s]", "")
                .replaceAll("\\s+", "-")
                .toLowerCase();
    }

    private Event getEventById(String eventId) {
        Event evento = this.eventRepository.findById(eventId).orElseThrow(() -> new EventNotFound("Event not found with ID: " + eventId));
        return evento;
    }

    public AttendeeIdDto registerAttendeeOnEvent(String eventId, AttendeeRequestDto attendeeRequestDto){
        this.attendeeService.verifyAttendeeSubscription(attendeeRequestDto.email(), eventId);
        Event evento = this.getEventById(eventId);
        List<Attendee>attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);

        if(evento.getMaximumAttendees() <= attendeeList.size()){
            throw  new EventFull("Event is full");
        }

        Attendee newAttendee = new Attendee();
        newAttendee.setName(attendeeRequestDto.name());
        newAttendee.setEmail(attendeeRequestDto.email());
        newAttendee.setEvent(evento);
        newAttendee.setCreatedAt(LocalDateTime.now());

        this.attendeeService.registerAttendee(newAttendee);

        return new AttendeeIdDto(newAttendee.getId());
    }

}
