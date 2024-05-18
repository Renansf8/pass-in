package renanferreira.com.passin.dto.event;

import lombok.Data;
import renanferreira.com.passin.domain.event.EventEntity;

@Data
public class EventRespondeDTO {
  EventDTO eventDto;

  public EventRespondeDTO(EventEntity event, Integer numberOfAttendees) {
    this.eventDto = new EventDTO(event.getId(), event.getTitle(), event.getSlug(), event.getMaximumAttendees(), numberOfAttendees);
  }
}
