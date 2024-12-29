package com.example.hhpluscleanarch.infrastructure.event;

import com.example.hhpluscleanarch.domain.event.entity.Event;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventJpaRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e " +
            "WHERE e.applyStartDate <= :currentDate " +
            "AND e.applyEndDate >= :currentDate " +
            "AND e.currentApplicants <= e.maxCapacity")
    List<Event> findAllValidEvents(LocalDate currentDate);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Event e WHERE e.eventId = :eventId")
    Event getEventById(Long eventId);
}
