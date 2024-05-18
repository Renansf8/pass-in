package renanferreira.com.passin.services;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import renanferreira.com.passin.domain.attendee.AttendeeEntity;
import renanferreira.com.passin.domain.event.EventEntity;
import renanferreira.com.passin.domain.event.exceptions.EventFullException;
import renanferreira.com.passin.domain.event.exceptions.EventNofFoundException;
import renanferreira.com.passin.dto.attendee.AttendeeIdDTO;
import renanferreira.com.passin.dto.attendee.AttendeeRequestDTO;
import renanferreira.com.passin.dto.event.EventIdDTO;
import renanferreira.com.passin.dto.event.EventRequestDTO;
import renanferreira.com.passin.dto.event.EventRespondeDTO;
import renanferreira.com.passin.repositories.EventRepository;

@Service
// Vai gerar um construtor pra classe
@RequiredArgsConstructor
public class EventService {
  
  private final EventRepository eventRepository;

  private final AttendeeService attendeeService;

  public EventRespondeDTO getEventDetail(String eventId) {
    EventEntity event = this.getEventById(eventId);
    List<AttendeeEntity> attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);
    return new EventRespondeDTO(event, attendeeList.size());
  }

  public EventIdDTO createEvent(EventRequestDTO eventDto) {
    EventEntity newEvent = new EventEntity();
    newEvent.setTitle(eventDto.title());
    newEvent.setDetails(eventDto.details());
    newEvent.setMaximumAttendees(eventDto.maximumAttendees());
    newEvent.setSlug(this.createSlug(eventDto.title()));

    this.eventRepository.save(newEvent);

    return new EventIdDTO(newEvent.getId());
  }

  public AttendeeIdDTO registerAttendeeOnEvent(String eventId, AttendeeRequestDTO attendeeRequestDTO) {
    this.attendeeService.verifyAttendeeSubscription(attendeeRequestDTO.email(), eventId);
    EventEntity event = this.getEventById(eventId);
    List<AttendeeEntity> attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);

    if (event.getMaximumAttendees() <= attendeeList.size()) throw new EventFullException("Event is full");

    AttendeeEntity newAttendee = new AttendeeEntity();
    newAttendee.setName(attendeeRequestDTO.name());
    newAttendee.setEmail(attendeeRequestDTO.email());
    newAttendee.setEvent(event);
    newAttendee.setCreatedAt(LocalDateTime.now());
    this.attendeeService.registerAttendee(newAttendee);

    return new AttendeeIdDTO(newAttendee.getId());
  }

  private EventEntity getEventById(String eventId) {
    EventEntity event = this.eventRepository.findById(eventId).orElseThrow(() -> {
      return new EventNofFoundException("Evento não encontrado com o ID: " + eventId);
    });
    return event;
  }

  private String createSlug(String text){
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
                         .replaceAll("[^\\w\\s]", "")
                         .replaceAll("\\s+", "-")
                         .toLowerCase();
    }
}
