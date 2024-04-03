package rockeseat.com.passin.dto.event;

import lombok.Getter;
import rockeseat.com.passin.domain.event.Event;

@Getter
public class EventResponseDto {
    EventDetailDto event;

    public EventResponseDto(Event event, Integer numberOfAttendees){
        this.event = new EventDetailDto(event.getId(), event.getTitle(), event.getDetails(), event.getSlug(), event.getMaximumAttendees(), numberOfAttendees);
    }
}
