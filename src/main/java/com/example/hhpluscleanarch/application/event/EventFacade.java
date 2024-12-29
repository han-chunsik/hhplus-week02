package com.example.hhpluscleanarch.application.event;

import com.example.hhpluscleanarch.domain.event.service.EventService;
import com.example.hhpluscleanarch.interfaces.api.event.EventApplicationResponse;
import com.example.hhpluscleanarch.interfaces.api.event.EventResponse;
import org.springframework.stereotype.Service;

@Service
public class EventFacade {
    private final EventService eventService;

    public EventFacade(EventService eventService) {
        this.eventService = eventService;
    }

    // 특강 신청
    public EventApplicationResponse applyForEvent(Long userId, Long eventId) {
        return eventService.applyForEvent(userId, eventId);
    }

    // 특강 신청 가능 목록 조회
    public EventResponse getAvailableEvents(String date) {
        return eventService.getAvailableEvents(date);
    }

    // 특강 신청 완료 목록 조회
    public EventResponse getCompletedEvents(Long userId) {
        return eventService.getCompletedEvents(userId);
    }
}
