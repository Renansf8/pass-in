package renanferreira.com.passin.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import renanferreira.com.passin.domain.attendee.AttendeeEntity;
import renanferreira.com.passin.domain.checkIn.CheckInEntity;
import renanferreira.com.passin.domain.checkIn.exceptions.CheckInAlreadyExistsExcepetion;
import renanferreira.com.passin.repositories.CheckInRepository;

@Service
@RequiredArgsConstructor
public class CheckInService {

  private final CheckInRepository checkInRepository;

  public void registerCheckIn(AttendeeEntity attendee) {
    this.verifyCheckInExists(attendee.getId());

    CheckInEntity newCheckIn = new CheckInEntity();
    newCheckIn.setAttendee(attendee);
    newCheckIn.setCreatedAt(LocalDateTime.now());
    this.checkInRepository.save(newCheckIn);
  }

  private void verifyCheckInExists(String attendeeId) {
    Optional<CheckInEntity> isCheckedIn = this.getCheckIn(attendeeId);
    if (isCheckedIn.isPresent())
      throw new CheckInAlreadyExistsExcepetion("Attendee already checked in");
  }

  public Optional<CheckInEntity> getCheckIn(String attendeeId) {
    return this.checkInRepository.findByAttendeeId(attendeeId);
  }
}
