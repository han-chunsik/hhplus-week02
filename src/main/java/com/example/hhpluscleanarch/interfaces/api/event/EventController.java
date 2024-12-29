package com.example.hhpluscleanarch.interfaces.api.event;

import com.example.hhpluscleanarch.application.event.EventFacade;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
public class EventController {

    private final EventFacade eventFacade;
    public EventController(EventFacade eventFacade) {
        this.eventFacade = eventFacade;
    }


    // 특강 신청 API
    @PostMapping("/applications")
    public EventApplicationResponse applyForEvent(@RequestBody EventRequest request) {
        return eventFacade.applyForEvent(request.getUserId(),request.getEventId());
    }

    // 특강 신청 가능 목록 조회 API
    @GetMapping("/available")
    public EventResponse getAvailableEvents(@RequestParam String date) {
        return eventFacade.getAvailableEvents(date);
    }

    // 특강 신청 완료 목록 조회 API
    @GetMapping("/{userId}/completed")
    public EventResponse getCompletedEvents(@PathVariable long userId) {
        return eventFacade.getCompletedEvents(userId);
    }
}
