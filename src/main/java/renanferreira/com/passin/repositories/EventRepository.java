package renanferreira.com.passin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import renanferreira.com.passin.domain.event.EventEntity;

public interface EventRepository extends JpaRepository<EventEntity, String> {
}
