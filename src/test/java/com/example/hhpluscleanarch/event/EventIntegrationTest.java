package com.example.hhpluscleanarch.event;

import com.example.hhpluscleanarch.domain.event.service.EventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
public class EventIntegrationTest {
    private static final Logger log = LoggerFactory.getLogger(EventIntegrationTest.class);

    @Autowired
    private EventService eventService;

    @Container
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest")
            .withDatabaseName("test")
            .withUsername("root")
            .withPassword("test123")
            .withInitScript("testcontainers/mysql/init.sql");

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @Nested
    @DisplayName("동시성 제어 통합 테스트")
    class SuccessCase {
        @Test
        @DisplayName("40명의 유저가 한번에 특강 신청 요청을 수행할때, 30명만 성공")
        public void 동시에_동일한_특강에_대해_40명이_신청했을_때_30명만_성공하는_것을_검증() throws Exception {
            //given
            int threadCount = 40;
            long eventId = 1L;

            CountDownLatch doneSignal = new CountDownLatch(threadCount);
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

            AtomicLong successfulRequestCount = new AtomicLong(0);
            AtomicLong failedRequestCount = new AtomicLong(0);

            // when
            for (long i = 1; i <= threadCount; i++) {
                final long userId = i;
                executorService.execute(() -> {
                    try {
                        eventService.applyForEvent(userId, eventId);
                        System.out.println(userId);
                        successfulRequestCount.incrementAndGet();
                    } catch (Exception e) {
                        log.error("An error occurred: ", e);
                        failedRequestCount.incrementAndGet();
                    } finally {
                        doneSignal.countDown(); // 각 스레드가 종료될 때마다 호출
                    }
                });
            }
            doneSignal.await();
            executorService.shutdown();

            assertEquals(30, successfulRequestCount.get());
            assertEquals(10, failedRequestCount.get());
            System.out.println("실패한 요청 수: " + failedRequestCount.get());
        }

        @Test
        @DisplayName("동일한 유저 정보로 같은 특강을 5번 신청했을 때, 한 번만 성공")
        public void 동일한_유저_정보로_같은_특강을_5번_신청했을_때_1번만_성공하는_것을_검증() throws Exception {
            //given
            int threadCount = 5;
            long eventId = 2L;
            long userId = 1L;

            CountDownLatch doneSignal = new CountDownLatch(threadCount);
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

            AtomicLong successfulRequestCount = new AtomicLong(0);
            AtomicLong failedRequestCount = new AtomicLong(0);

            // when
            for (long i = 1; i <= threadCount; i++) {
                executorService.execute(() -> {
                    try {
                        eventService.applyForEvent(userId, eventId);
                        successfulRequestCount.incrementAndGet();
                    } catch (Exception e) {
                        log.error("An error occurred: ", e);
                        failedRequestCount.incrementAndGet();
                    } finally {
                        doneSignal.countDown(); // 각 스레드가 종료될 때마다 호출
                    }
                });
            }
            doneSignal.await();
            executorService.shutdown();

            assertEquals(1, successfulRequestCount.get());
            assertEquals(4, failedRequestCount.get());
            System.out.println("실패한 요청 수: " + failedRequestCount.get());
        }
    }
}
