package renanferreira.com.passin.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import renanferreira.com.passin.domain.checkIn.CheckInEntity;

public interface CheckInRepository extends JpaRepository<CheckInEntity, Integer> {
  
  Optional<CheckInEntity> findByAttendeeId(String attendeeId); 
}
