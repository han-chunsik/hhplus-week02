package com.example.hhpluscleanarch.interfaces.api.event;

import com.example.hhpluscleanarch.domain.event.entity.EventApplication;
import com.example.hhpluscleanarch.interfaces.api.common.CommonResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventApplicationResponse extends CommonResponse {
    private EventApplication eventApplication;
}
