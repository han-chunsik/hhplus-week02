package com.example.hhpluscleanarch.domain.event.service;

import com.example.hhpluscleanarch.common.code.ResponseCode;
import com.example.hhpluscleanarch.domain.event.entity.Event;
import com.example.hhpluscleanarch.domain.event.entity.EventApplication;
import com.example.hhpluscleanarch.domain.event.repository.EventRepository;
import com.example.hhpluscleanarch.interfaces.api.event.EventApplicationResponse;
import com.example.hhpluscleanarch.interfaces.api.event.EventResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
    /**
     * 신청 가능 특강 목록 조회
     * @param dateString 날짜
     * @return 신청 가능 특강 목록
     */
    public EventResponse getAvailableEvents (String dateString){
        // 1. 파라미터 타입 변환(String -> Date)
        LocalDate date = LocalDate.parse(dateString);

        // 2. 신청 가능 특강 목록 조회
        EventResponse result = new EventResponse();
        List<Event> eventList = eventRepository.findAllValidEvents(date);
        result.setEvents(eventList);
        result.setCode(ResponseCode.SUCCESS_EVENT_LIST_RETRIEVED.getCode());
        result.setMessage(ResponseCode.SUCCESS_EVENT_LIST_RETRIEVED.getMessage());

        return result;
    }

    /**
     * 특강 신청
     * @param userId 사용자 ID
     * @param eventId 특강 ID
     * @return 신청 완료 결과
     */
    @Transactional
    public EventApplicationResponse applyForEvent (Long userId, Long eventId) {

        // 1. 해당 event의 최대정원과 현재 신청 인원 조회, 해당 로우 잠금
        Event event = eventRepository.getEventByIdWithLock(eventId);

        // 2. 최대 정원 체크, 오류 발생 시 해당 로우 잠금 해제
        int eventMaxCapacity = event.getMaxCapacity();
        int eventCurrentApplicants = event.getCurrentApplicants();
        if (eventMaxCapacity == eventCurrentApplicants) {
            throw new RuntimeException("최대 정원은 " + eventMaxCapacity + "명이며, 정원 초과 입니다.");
        }

        // 3. 기 신청 여부 체크, 오류 발생 시 해당 로우 잠금 해제
        int eventApplicationCount = eventRepository.countEventApplicationByUserAndEventId(userId, eventId);
        if (eventApplicationCount != 0) {
            throw new RuntimeException("이미 신청한 특강입니다.");
        }

        // 4. 신청 완료 테이블 저장
        eventRepository.saveEventApplication(userId, eventId);

        // 5. 해당 특강 currentApplicants +1, 해당 로우 업데이트 후 잠금 해제
        eventRepository.saveEventCurrentApplicants(eventId);

        // 6. 신청 완료 테이블 리턴
        EventApplication eventApplication = eventRepository.getEventApplicationByUserAndEventId(userId, eventId);

        EventApplicationResponse result = new EventApplicationResponse();
        result.setEventApplication(eventApplication);
        result.setCode(ResponseCode.SUCCESS_EVENT_APPLICATION_REGISTERED.getCode());
        result.setMessage(ResponseCode.SUCCESS_EVENT_APPLICATION_REGISTERED.getMessage());

        return result;
    }

    /**
     * 신청 완료 특강 목록 조회
     * @param userId 조회할 사용자 ID
     * @return 신청 완료 특강 목록
     */
    public EventResponse getCompletedEvents(long userId) {
        // 1. 사용자 신청 완료 목록 조회
        List<EventApplication> eventApplicationList = eventRepository.getEventApplicationByUserId(userId);

        // 2. 특강 목록 조회
        List<Long> eventIdList = new ArrayList<>();
        for(EventApplication eventApplication : eventApplicationList) {
            eventIdList.add(eventApplication.getEventId());
        }

        List<Event> userApplicationEventList = eventRepository.getEventFindAllById(eventIdList);
        EventResponse result = new EventResponse();
        result.setEvents(userApplicationEventList);
        result.setCode(ResponseCode.SUCCESS_EVENT_APPLICATION_LIST_RETRIEVED.getCode());
        result.setMessage(ResponseCode.SUCCESS_EVENT_APPLICATION_LIST_RETRIEVED.getMessage());

        return result;
    }
}
