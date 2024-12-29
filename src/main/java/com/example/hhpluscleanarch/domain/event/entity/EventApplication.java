package com.example.hhpluscleanarch.domain.event.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "event_application")
public class EventApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long applicationId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "reg_dt", nullable = false)
    private LocalDateTime regDt = LocalDateTime.now();

    public EventApplication(Long applicationId, Long userId, Long eventId, LocalDateTime regDt) {
        this.applicationId = applicationId;
        this.userId = userId;
        this.eventId = eventId;
        this.regDt = regDt;
    }

    public EventApplication() {

    }
}
