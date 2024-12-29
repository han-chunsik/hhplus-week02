# 특강 신청 서비스
> `special lecture` 가 특강을 표현하기에 가장 직관적이라 생각하였으나, 컬럼명, API endpoint로 사용하기엔 긴 느낌이 있어 `event` 사용

## 1️⃣ 기능 및 정책 정의
### 기능
- 항해 플러스 토요일 특강을 신청할 수 있는 서비스

### 정책
- 특강은 선착순 30명만 신청 가능
- 동일한 신청자는 동일한 강의에 대해서 한 번의 수강 신청만 성공
- 사용자는 각 특강에 신청하기 전 목록을 조회해 볼 수 있어야 함
- 로그인, 회원가입, 특강 신청 취소는 고려하지 않음
- (추가)사용자는 특강 신청 가능 일자에만 신청 가능함(정원이 가득 차 있거나, 특강 신청 가능 일자 외에는 신청 불가)
- (추가)사용자는 신청가능 특강 목록 조회 시 특정 날짜를 선택하면, 해당 날짜에 신청 가능한 특강만 조회 가능함

## 2️⃣ Use Case
![image](https://github.com/user-attachments/assets/3bfd06f8-88a9-4e39-8746-24f02664a733)

|Use Case|특강 신청 서비스|
|--|--|
|설명|사용자가 신청가능 특강 목록에서 원하는 특강을 신청하는 기능|
|주요 행위자|특강 신청 사용자|
|트리거|사용자가 신청 완료 특강 목록을 조회하거나, 신청 가능 특강 목록을 조회하여 신청한다.|
|기본 흐름|1. 사용자가 특정 날짜의 신청가능 특강 목록을 조회한다. <br> 2. 원하는 특강을 신청한다. <br> 3. 정원초과, 기 신청자가 아닐경우 신청에 성공한다. <br> 4. 신청 완료 특강 목록을 조회한다.(신청목록이 없어도 가능)|

## 3️⃣ DB
### 필요 정보 정의
|구분|필요 정보|
|---|------|
|사용자|사용자ID, 이름, 이메일, 가입 날짜|
|특강|특강ID, 특강 명, 강연자 명, 강연 장소, 강연 일자, 최대 정원, 현재 신청 인원, 신청 시작 일자, 신청 마감 일자|
|신청 완료|신청완료ID, 신청자ID, 특강ID|

### 개념 설계
![image](https://github.com/user-attachments/assets/2a69925b-d0f6-43b9-a81d-7f29e3253e9a)  

- **사용자 - 신청(신청 완료)**
    - 유형: 1:n
    - 각 사용자는 여러 특강에 신청 가능함
- **특강 - 신청(신청 완료)**
    - 유형: 1:n
    - 하나의 특강에는 여러 사용자가 신청을 함
- **사용자 - 특강**
    - 유형: n:m
    - 하나의 사용자는 여러 특강에 신청할 수 있고, 하나의 특강도 여러 사용자의 신청을 받을 수 있음

### ERD
![image](https://github.com/user-attachments/assets/7b73c365-e852-4a29-808e-e2e1eb596bef)

- 한 명의 사용자는 여러 신청을 할 수 있지만, 신청이 없을수도, 여러개 있을 수 있음
- 하나의 신청은 반드시 하나의 특강에 속해야 하지만, 하나의 특강에는 여러 신청이 있을 수 있음

**사용자 테이블 (user)**

| 컬럼명      | 타입           | 제약조건          | 설명                     |이유                                                   |
|----------|--------------|-------------------|--------------------|-----------------------------------------------------|
| id       | LONG         | PRIMARY KEY, NOT NULL | 사용자의 고유 ID   |각 사용자를 고유하게 식별하기 위함, 다른 테이블과 관계를 맺을 때 사용|
| name     | VARCHAR(100) | NOT NULL          | 사용자의 이름          |사용자 확인 시 필요                                     |
| email    | VARCHAR(100) | NOT NULL, UNIQUE  | 사용자의 이메일, 고유값  |이메일은 고유해야하며, 사용자 확인 시 필요                    |
| reg_dt   | DATETIME     | NOT NULL          | 데이터 생성 시간       |데이터 정렬 및 분석에 활용                                 |

**신청 완료 테이블 (event_application)**

| 컬럼명 | 타입     | 제약조건                | 설명                    |이유                                                      |
|---|---------|-----------------------|--------------------------|------------------------------------------------------------|
| id| LONG  | PRIMARY KEY, NOT NULL | 신청 완료 항목의 고유 ID      |각 신청 완료 항목을 고유하게 식별하기 위함, 다른 테이블과 관계를 맺을 때 사용 |
| user_id | LONG  | NOT NULL              | 신청한 사용자의 ID           |신청한 사용자를 식별하기 위해 사용자 테이블의 user_id를 참조             |
| event_id | LONG  | NOT NULL              | 신청한 특강의 ID            |신청한 특강을 식별하기 위해 특강 테이블의 event_id                     |
| reg_dt | DATETIME | NOT NULL              | 데이터 생성 시간             |데이터 정렬 및 분석에 활용        |

**특강 테이블 (event)**

| 컬럼명              | 타입          | 제약조건               | 설명                     |이유                                                 |
|------------------|-------------|----------------------|-------------------------|----------------------------------------------------|
| id               | LONG      | PRIMARY KEY, NOT NULL| 특강의 고유 ID             |각 특강을 고유하게 식별하기 위함, 다른 테이블과 관계를 맺을 때 사용 |
| event_name       | VARCHAR(200)| NOT NULL             | 특강의 이름                |특강을 찾을 때 중요한 정보로 활용                           |
| presenter_name   | VARCHAR(100)| NOT NULL             | 특강 강연자의 이름           |특강을 찾는 데 필요한 정보로 활용                           |
| event_location   | VARCHAR(100)| NOT NULL             | 특강이 열리는 장소           |특강을 찾는 데 필요한 정보로 활용                           |
| event_dt         | DATETIME    | NOT NULL             | 특강이 열리는 날짜와 시간      |특강 일정 관리와 예약에 활용                              |
| apply_start_date | DATE    | NOT NULL             | 신청이 시작된 날짜와 시간     |신청 가능 기간을 관리하는 데 필요                           |
| apply_end_date   | DATE    | NOT NULL             | 신청이 마감된 날짜와 시간     |신청 가능 기간을 관리하는 데 필요                            |
| max_capacity     | INT         | NOT NULL             | 특강의 최대 정원 (기본값:0)   |신청을 받을 수 있는 인원수를 제한하는 데 사용                  |
| current_applicants | INT         | NOT NULL             | 현재 신청한 인원 수 (기본값:0)|정원에 대한 정보를 실시간으로 관리                           |
| reg_dt           | DATETIME    | NOT NULL             | 데이터 생성 시간            |데이터 정렬 및 분석에 활용                                 |


## 4️⃣ API 명세
| API                         | 메소드 | URL                             | 설명                        |
|----------------------------|--------|--------------------------------|-----------------------------|
| 특강 신청 API                | POST   | `/event/applications`           | 사용자 특강 신청                |
| 특강 신청 가능 목록 조회 API     | GET    | `/event/available`            | 특정 날짜의 신청 가능한 특강 목록 조회|
| 특강 신청 완료 목록 조회 API     | GET    | `/event/{user_id}/completed`  | 사용자가 신청한 특강 목록 조회    |


### 특강 신청 API - [동시성 제어 설계](https://github.com/han-chunsik/hhplus-w02/wiki/%EB%8F%99%EC%8B%9C%EC%84%B1-%7C-%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%A0%9C%EC%96%B4)
- **Endpoint**
    - POST `/event/applications`
- **Parameter**
    - (request body)user_id: (long) 특강 신청 유저의 ID
    - (request body)event_id: (long) 신청하려는 특강 ID
- **Response**
  ```json
    {
        "code": 20001,
        "message": "특강 신청이 완료되었습니다.",
        "eventApplication": {
            "applicationId": 9,
            "userId": 5,
            "eventId": 2,
            "regDt": "2024-12-27T01:38:50.026821"
        }
    }
  ```
- **기능 요구사항**
    - 동일한 신청자는 동일한 강의에 대해서 한 번의 수강 신청만 성공
    - 특강은 선착순 30명만 신청 가능
    - 이미 신청자가 30명이 초과 되면 이후 신청은 실패
    - 신청 기간 외 신청은 실패
- **단위 테스트 케이스**
    - Service
        - 이미 신청한 경우 실패
        - 선착순 인원이 초과된 경우 실패

**특강 신청 가능 목록 조회 API**
- **Endpoint**
    - GET `/event/available`
- **Parameter**
    - (query)date: (String) 조회할 날짜 (형식: yyyy-MM-dd)
- **Response**
  ```json
    {
        "code": 20002,
        "message": "신청 가능한 특강 목록을 조회했습니다.",
        "events": [
            {
            "eventId": 1,
            "eventName": "Tech Conference 2024",
            "presenterName": "John Doe",
            "eventLocation": "Seoul Convention Center",
            "eventDt": "2024-12-31T14:00:00",
            "applyStartDate": "2024-12-01",
            "applyEndDate": "2024-12-30",
            "maxCapacity": 100,
            "currentApplicants": 57,
            "regDt": "2024-11-01T10:00:00"
            }
        ]
    }
  ```
- **기능 요구사항**
    - 날짜별로 현재 신청 가능한 특강 목록을 조회

### 특강 신청 완료 목록 조회 API
- **Endpoint**
    - GET `/event/{user_id}/completed`
- **Path Parameter**
    - (query)user_id: (long) 조회할 유저의 ID
- **Response**
  ```json
    {
        "code": 20003,
        "message": "특강 신청 목록을 조회했습니다.",
        "events": [
            {
            "eventId": 1,
            "eventName": "Tech Conference 2024",
            "presenterName": "John Doe",
            "eventLocation": "Seoul Convention Center",
            "eventDt": "2024-12-31T14:00:00",
            "applyStartDate": "2024-12-01",
            "applyEndDate": "2024-12-30",
            "maxCapacity": 100,
            "currentApplicants": 57,
            "regDt": "2024-11-01T10:00:00"
            },
            {
            "eventId": 2,
            "eventName": "AI and Machine Learning Workshop",
            "presenterName": "Jane Smith",
            "eventLocation": "Busan Exhibition Hall",
            "eventDt": "2025-01-15T10:00:00",
            "applyStartDate": "2024-12-15",
            "applyEndDate": "2025-01-10",
            "maxCapacity": 50,
            "currentApplicants": 24,
            "regDt": "2024-12-10T09:00:00"
            }
        ]
    }
  ```
- **기능 요구사항**
    - 유저 ID를 받아 해당 사용자의 신청 완료 목록 조회
