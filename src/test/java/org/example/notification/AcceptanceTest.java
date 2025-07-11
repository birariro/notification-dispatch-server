package org.example.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class AcceptanceTest {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String URL = "http://localhost:8080/notification";

    void testHighLoadNotificationRegistration() throws InterruptedException {
        int totalRequests = 100;
        int threadPoolSize = 100;

        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
        CountDownLatch latch = new CountDownLatch(totalRequests);
        List<Future<ResponseEntity<Void>>> futures = new ArrayList<>();


        for (int i = 0; i < totalRequests; i++) {
            futures.add(executor.submit(() -> {
                try {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    String payload = objectMapper.writeValueAsString(
                            Map.of(
                                    "memberId", "member_test",
                                    "recipientId", UUID.randomUUID().toString(),
                                    "title", "테스트 제목",
                                    "contents", "테스트 내용",
                                    "channel", "EMAIL",
                                    "scheduledDate", ""
                            )
                    );

                    HttpEntity<String> request = new HttpEntity<>(payload, headers);
                    ResponseEntity<Void> response = restTemplate.postForEntity(URL, request, Void.class);
                    return response;
                } catch (Exception e) {
                    System.err.println("Error during request: " + e.getMessage());
                    return null;
                } finally {
                    latch.countDown();
                }
            }));
        }

        latch.await();
        executor.shutdown();
    }
}
