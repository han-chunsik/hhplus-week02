package com.example.hhpluscleanarch.common.code;

import lombok.Getter;

@Getter
public enum ResponseCode {
    SUCCESS_EVENT_APPLICATION_REGISTERED(20001, "특강 신청이 완료되었습니다."),
    SUCCESS_EVENT_LIST_RETRIEVED(20002, "신청 가능한 특강 목록을 조회했습니다."),
    SUCCESS_EVENT_APPLICATION_LIST_RETRIEVED(20003, "특강 신청 목록을 조회했습니다.");

    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
