package renanferreira.com.passin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import renanferreira.com.passin.dto.attendee.AttendeeIdDTO;
import renanferreira.com.passin.dto.attendee.AttendeeListResponseDTO;
import renanferreira.com.passin.dto.attendee.AttendeeRequestDTO;
import renanferreira.com.passin.dto.event.EventIdDTO;
import renanferreira.com.passin.dto.event.EventRequestDTO;
import renanferreira.com.passin.dto.event.EventRespondeDTO;
import renanferreira.com.passin.services.AttendeeService;
import renanferreira.com.passin.services.EventService;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

  private final EventService eventService;

  private final AttendeeService attendeeService;
  
  @GetMapping("/{id}")
  public ResponseEntity<EventRespondeDTO> getEvent(@PathVariable String id) {
    EventRespondeDTO event = this.eventService.getEventDetail(id);
    return ResponseEntity.ok().body(event);
  }

  @PostMapping("/")
  public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO eventRequestDTO, UriComponentsBuilder uriComponentsBuilder) {
    EventIdDTO createdEvent = this.eventService.createEvent(eventRequestDTO);

    // Para o user buscar a informação do evento que foi criado
    var uri = uriComponentsBuilder.path("/events/{id}").buildAndExpand(createdEvent.eventId()).toUri();

    return ResponseEntity.created(uri).body(createdEvent);
  }

   @PostMapping("/{eventId}/attendees")
  public ResponseEntity<Object> registerParticipant(@PathVariable String eventId, @RequestBody AttendeeRequestDTO attendeeRequestDTO, UriComponentsBuilder uriComponentsBuilder) {
    try {

      AttendeeIdDTO attendeeIdDTO = this.eventService.registerAttendeeOnEvent(eventId, attendeeRequestDTO);
      
      // Para o user buscar a informação do evento que foi criado
      var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/badge").buildAndExpand(attendeeIdDTO.attendeeId()).toUri();
      
      return ResponseEntity.created(uri).body(attendeeIdDTO);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/attendees/{id}")
  public ResponseEntity<AttendeeListResponseDTO> getEventAttendees(@PathVariable String id) {
    AttendeeListResponseDTO attendeeList = this.attendeeService.getEventsAttendee(id);
    return ResponseEntity.ok().body(attendeeList);
  }
}
