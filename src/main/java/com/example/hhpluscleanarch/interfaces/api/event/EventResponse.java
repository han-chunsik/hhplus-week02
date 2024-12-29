package com.example.hhpluscleanarch.interfaces.api.event;

import com.example.hhpluscleanarch.domain.event.entity.Event;
import com.example.hhpluscleanarch.interfaces.api.common.CommonResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EventResponse extends CommonResponse {
    private List<Event> events;
}
