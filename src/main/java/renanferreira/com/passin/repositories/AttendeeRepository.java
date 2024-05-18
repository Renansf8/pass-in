package renanferreira.com.passin.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import renanferreira.com.passin.domain.attendee.AttendeeEntity;

public interface AttendeeRepository extends JpaRepository<AttendeeEntity, String> {
  List<AttendeeEntity> findByEventId(String eventId);
  Optional<AttendeeEntity> findByEventIdAndEmail(String eventId, String email);
}
