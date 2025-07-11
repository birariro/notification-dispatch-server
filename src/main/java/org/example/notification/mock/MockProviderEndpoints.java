package org.example.notification.mock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RestController
public class MockProviderEndpoints {

    @PostMapping("/send/sms")
    public ResponseEntity<Map<String, Object>> sendSms(@RequestBody Map<String, Object> request) {
        log.info("Mock SMS API called with: {}", request);

        double randomValue = ThreadLocalRandom.current().nextDouble();
        boolean success = randomValue < 0.5;
        return ResponseEntity.status(success ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", success ? "success" : "failed"));
    }
    
    @PostMapping("/send/push")
    public ResponseEntity<Map<String, Object>> sendPush(@RequestBody Map<String, Object> request) {
        log.info("Mock PUSH API called with: {}", request);

        double randomValue = ThreadLocalRandom.current().nextDouble();
        boolean success = randomValue < 0.5;
        return ResponseEntity.status(success ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", success ? "success" : "failed"));
    }
    
    @PostMapping("/send/email")
    public ResponseEntity<Map<String, Object>> sendEmail(@RequestBody Map<String, Object> request) {
        log.info("Mock EMAIL API called with: {}", request);

        double randomValue = ThreadLocalRandom.current().nextDouble();
        boolean success = randomValue < 0.5;
        return ResponseEntity.status(success ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", success ? "success" : "failed"));
    }
}
