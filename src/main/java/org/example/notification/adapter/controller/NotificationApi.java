package org.example.notification.adapter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.notification.Message;
import org.example.notification.Notification;
import org.example.notification.Recipient;
import org.example.notification.Sender;
import org.example.notification.application.Register;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
class NotificationApi {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    private final Register register;

    @PostMapping("/notification")
    ResponseEntity<Void> registrationNotification(@Valid @RequestBody RegistrationRequest request) {

        Sender sender = Sender.of(request.memberId());
        Recipient recipient = Recipient.of(request.recipientId(), request.channel());
        Message message = Message.of(request.title(), request.contents());
        String scheduledDate = request.scheduledDate();

        if (StringUtils.hasText(scheduledDate)) {
            register.reservation(sender, recipient, message, LocalDateTime.parse(scheduledDate, FORMATTER));
        } else {
            register.registration(sender, recipient, message);
        }
        return ResponseEntity.created(URI.create("/notification/senders/" + request.memberId())).build();
    }

}
