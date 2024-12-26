package com.example.hhpluscleanarch.domain.event.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long eventId;

    @Column(name = "event_name", nullable = false, length = 200)
    private String eventName;

    @Column(name = "presenter_name", nullable = false, length = 100)
    private String presenterName;

    @Column(name = "event_location", nullable = false, length = 100)
    private String eventLocation;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDt;

    @Column(name = "apply_start_date", nullable = false)
    private LocalDate applyStartDate;

    @Column(name = "apply_end_date", nullable = false)
    private LocalDate applyEndDate;

    @Column(name = "max_capacity", nullable = false)
    private int maxCapacity;

    @Column(name = "current_applicants", nullable = false)
    private int currentApplicants;

    @Column(name = "reg_dt", nullable = false)
    private LocalDateTime regDt = LocalDateTime.now();

    public Event(Long eventId, String eventName, String presenterName, String eventLocation, LocalDateTime eventDt, LocalDate applyStartDate, LocalDate applyEndDate,int maxCapacity, int currentApplicants, LocalDateTime regDt) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.presenterName = presenterName;
        this.eventLocation = eventLocation;
        this.eventDt = eventDt;
        this.applyStartDate = applyStartDate;
        this.applyEndDate = applyEndDate;
        this.maxCapacity = maxCapacity;
        this.currentApplicants = currentApplicants;
        this.regDt = regDt;
    }

    public Event() {

    }
}
