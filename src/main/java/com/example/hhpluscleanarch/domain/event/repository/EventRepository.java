package com.example.hhpluscleanarch.domain.event.repository;

import com.example.hhpluscleanarch.domain.event.entity.Event;
import com.example.hhpluscleanarch.domain.event.entity.EventApplication;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository {
    List<Event> findAllValidEvents(LocalDate currentDate);
    List<Event> getEventFindAllById(List<Long> eventIds);
    Event saveEventCurrentApplicants(Long eventId);
    Event getEventByIdWithLock(Long eventId);
    EventApplication getEventApplicationByUserAndEventId(Long userId, Long eventId);
    List<EventApplication> getEventApplicationByUserId(Long userId);
    int countEventApplicationByUserAndEventId(Long userId, Long eventId);
    void saveEventApplication(Long userId, Long eventId);
}
