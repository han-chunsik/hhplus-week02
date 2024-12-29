package com.example.hhpluscleanarch.event;

import com.example.hhpluscleanarch.domain.event.entity.Event;
import com.example.hhpluscleanarch.domain.event.repository.EventRepository;
import com.example.hhpluscleanarch.domain.event.service.EventService;
import com.example.hhpluscleanarch.interfaces.api.event.EventRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);  // Mockito 초기화
    }

    @Nested
    @DisplayName("applyForEventTest")
    class applyForEventTest {
        @Nested
        @DisplayName("실패 케이스")
        class FailCase {

            @Test
            @DisplayName("신청 특강의 정원이 초과한 경우 RuntimeException 에러 발생")
            void 신청_특강_정원_초과_실패() {
                // Given
                Long eventId = 1L;
                Long userId = 1L;

                Event event1 = new Event(
                        eventId,
                        "Tech Conference",
                        "John Doe",
                        "Seoul Convention Center",
                        LocalDateTime.of(2024, 5, 15, 10, 0),
                        LocalDate.of(2024, 12, 24),
                        LocalDate.of(2024, 12, 26),
                        30,
                        30,
                        LocalDateTime.now()
                );

                when(eventRepository.getEventByIdWithLock(eventId)).thenReturn(event1);

                // When/Then
                RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                    eventService.applyForEvent(userId, eventId);
                });
                assertEquals("최대 정원은 30명이며, 정원 초과 입니다.", exception.getMessage());
            }

            @Test
            @DisplayName("이미 특강을 신청한 경우 RuntimeException 에러 발생")
            void 기_특강_신청자_실패() {
                // Given
                Long eventId = 1L;
                Long userId = 1L;

                Event event1 = new Event(
                        eventId,
                        "Tech Conference",
                        "John Doe",
                        "Seoul Convention Center",
                        LocalDateTime.of(2024, 5, 15, 10, 0),
                        LocalDate.of(2024, 12, 24),
                        LocalDate.of(2024, 12, 26),
                        30,
                        29,
                        LocalDateTime.now()
                );

                when(eventRepository.getEventByIdWithLock(eventId)).thenReturn(event1);
                when(eventRepository.countEventApplicationByUserAndEventId(userId, eventId)).thenReturn(1);

                // When/Then
                RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                    eventService.applyForEvent(userId, eventId);
                });
                assertEquals("이미 신청한 특강입니다.", exception.getMessage());
            }
        }
    }

}
