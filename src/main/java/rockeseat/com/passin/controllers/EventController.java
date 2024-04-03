package rockeseat.com.passin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import rockeseat.com.passin.domain.attendee.Attendee;
import rockeseat.com.passin.dto.attendee.AttendeeIdDto;
import rockeseat.com.passin.dto.attendee.AttendeeRequestDto;
import rockeseat.com.passin.dto.attendee.AttendeesListResponseDto;
import rockeseat.com.passin.dto.event.EventIdDto;
import rockeseat.com.passin.dto.event.EventRequestDto;
import rockeseat.com.passin.dto.event.EventResponseDto;
import rockeseat.com.passin.services.AttendeeService;
import rockeseat.com.passin.services.EventService;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService service;
    private final AttendeeService attendeeService;
    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> getRoute(@PathVariable String eventId){
       EventResponseDto event = this.service.getEventDetail(eventId);
        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<EventIdDto> postEvent(@RequestBody EventRequestDto body, UriComponentsBuilder uriComponentsBuilder) {
        EventIdDto event = this.service.createEvent(body);

        var uri = uriComponentsBuilder.path("/events/{id}").buildAndExpand(event.eventId()).toUri();
        return ResponseEntity.created(uri).body(event);
    }

    @GetMapping("/attendees/{id}")
    public ResponseEntity<AttendeesListResponseDto> getEventAttendees(@PathVariable String id) {
        AttendeesListResponseDto listResponseDto = this.attendeeService.getEventsAttendee(id);

        return ResponseEntity.ok(listResponseDto);
    }

    @PostMapping("/{eventId}/attendees")
    public ResponseEntity<AttendeeIdDto> registerParticipant(@PathVariable String eventId, @RequestBody AttendeeRequestDto body, UriComponentsBuilder uriComponentsBuilder) {
        AttendeeIdDto attendeeIdDto = this.service.registerAttendeeOnEvent(eventId, body);

        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/badge").buildAndExpand(attendeeIdDto.attendeeId()).toUri();

        return ResponseEntity.created(uri).body(attendeeIdDto);
    }

}
