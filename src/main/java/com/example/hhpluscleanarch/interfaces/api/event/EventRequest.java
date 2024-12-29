package com.example.hhpluscleanarch.interfaces.api.event;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EventRequest {
    private long userId;
    private long eventId;

    public EventRequest(long userId, long eventId) {
        this.userId = userId;
        this.eventId = eventId;
    }
}
