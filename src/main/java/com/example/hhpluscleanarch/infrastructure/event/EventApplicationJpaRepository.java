package com.example.hhpluscleanarch.infrastructure.event;

import com.example.hhpluscleanarch.domain.event.entity.EventApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventApplicationJpaRepository  extends JpaRepository<EventApplication, Long> {
    @Query("SELECT COUNT(ea) FROM EventApplication ea WHERE ea.userId = :userId AND ea.eventId = :eventId")
    int countEventApplicationByUserAndEventId(Long userId, Long eventId);

    @Query("SELECT ea FROM EventApplication ea WHERE ea.userId = :userId AND ea.eventId = :eventId")
    EventApplication getEventApplicationByUserAndEventId(Long userId, Long eventId);

    @Query("SELECT ea FROM EventApplication ea WHERE ea.userId = :userId")
    List<EventApplication> getEventApplicationByUserId(Long userId);
}
