package renanferreira.com.passin.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import renanferreira.com.passin.domain.attendee.AttendeeEntity;
import renanferreira.com.passin.domain.attendee.exceptions.AttendeeAlreadyExistsException;
import renanferreira.com.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import renanferreira.com.passin.domain.checkIn.CheckInEntity;
import renanferreira.com.passin.dto.attendee.AttendeeBadgeDTO;
import renanferreira.com.passin.dto.attendee.AttendeeBadgeResponseDTO;
import renanferreira.com.passin.dto.attendee.AttendeeDetails;
import renanferreira.com.passin.dto.attendee.AttendeeListResponseDTO;
import renanferreira.com.passin.repositories.AttendeeRepository;

@Service
@RequiredArgsConstructor
public class AttendeeService {

  private final AttendeeRepository attendeeRepository;

  private final CheckInService checkInService;

  public List<AttendeeEntity> getAllAttendeesFromEvent(String eventId) {
    List<AttendeeEntity> attendeesList = this.attendeeRepository.findByEventId(eventId);
    return attendeesList;
  }

  public AttendeeListResponseDTO getEventsAttendee(String eventId) {
    List<AttendeeEntity> attendeesList = this.getAllAttendeesFromEvent(eventId);

    List<AttendeeDetails> attendeeDetailsList = attendeesList.stream().map(attendee -> {
      Optional<CheckInEntity> checkIn = this.checkInService.getCheckIn(attendee.getId());
      LocalDateTime checkInAt = checkIn.isPresent() ? checkIn.get().getCreatedAt() : null;
      return new AttendeeDetails(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(),
          checkInAt);
    }).toList();

    return new AttendeeListResponseDTO(attendeeDetailsList);
  }

  public void verifyAttendeeSubscription(String email, String eventId) {
    Optional<AttendeeEntity> isAttendeeRegistered = this.attendeeRepository.findByEventIdAndEmail(eventId, email);
    if (isAttendeeRegistered.isPresent())
      throw new AttendeeAlreadyExistsException("Attendee is already registered");
  }

  public AttendeeEntity registerAttendee(AttendeeEntity newAttendee) {
    this.attendeeRepository.save(newAttendee);
    return newAttendee;
  }

  public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
    AttendeeEntity attendee = this.getAttendee(attendeeId);

    var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/chech-in").buildAndExpand(attendeeId).toUri()
        .toString();
    AttendeeBadgeDTO badgeDTO = new AttendeeBadgeDTO(attendee.getName(), attendee.getEmail(), uri,
        attendee.getEvent().getId());
    return new AttendeeBadgeResponseDTO(badgeDTO);
  }

  public void checkInAttendee(String attendeeId) {
     AttendeeEntity attendee = this.getAttendee(attendeeId);
     this.checkInService.registerCheckIn(attendee);
  }

  public AttendeeEntity getAttendee(String attendeeId) {
    return this.attendeeRepository.findById(attendeeId)
        .orElseThrow(() -> {
          return new AttendeeNotFoundException("Attendee not found with id" + attendeeId);
        });
  }
}
