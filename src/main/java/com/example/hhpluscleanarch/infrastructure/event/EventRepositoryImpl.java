package com.example.hhpluscleanarch.infrastructure.event;

import com.example.hhpluscleanarch.domain.event.entity.Event;
import com.example.hhpluscleanarch.domain.event.entity.EventApplication;
import com.example.hhpluscleanarch.domain.event.repository.EventRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class EventRepositoryImpl implements EventRepository {
    private final EventJpaRepository eventJpaRepository;
    private final EventApplicationJpaRepository eventApplicationJpaRepository;

    public EventRepositoryImpl(EventJpaRepository eventJpaRepository, EventApplicationJpaRepository eventApplicationJpaRepository) {
        this.eventJpaRepository = eventJpaRepository;
        this.eventApplicationJpaRepository = eventApplicationJpaRepository;
    }

    @Override
    public List<Event> findAllValidEvents(LocalDate currentDate) {
        return eventJpaRepository.findAllValidEvents(currentDate);
    }

    @Override
    public Event saveEventCurrentApplicants(Long eventId) {
        Event event = eventJpaRepository.getEventByIdWithLock(eventId);
        event.setCurrentApplicants(event.getCurrentApplicants() + 1);
        return event;

    }

    @Override
    public List<Event> getEventFindAllById(List<Long> eventIdList){
        return eventJpaRepository.findAllById(eventIdList);
    }

    @Override
    public Event getEventByIdWithLock(Long eventId) {
        return eventJpaRepository.getEventByIdWithLock(eventId);
    }

    @Override
    public int countEventApplicationByUserAndEventId(Long userId, Long eventId) {
        return eventApplicationJpaRepository.countEventApplicationByUserAndEventId(userId, eventId);
    }

    @Override
    public void saveEventApplication(Long userId, Long eventId) {
        EventApplication eventApplication = new EventApplication();
        eventApplication.setUserId(userId);
        eventApplication.setEventId(eventId);
        eventApplicationJpaRepository.save(eventApplication);
    }

    @Override
    public EventApplication getEventApplicationByUserAndEventId(Long userId, Long eventId) {
        return eventApplicationJpaRepository.getEventApplicationByUserAndEventId(userId, eventId);
    }

    @Override
    public List<EventApplication> getEventApplicationByUserId(Long userId){
        return eventApplicationJpaRepository.getEventApplicationByUserId(userId);
    }
}
