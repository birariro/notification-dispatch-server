package org.example.notification.application.support;

import org.example.notification.Channel;
import org.example.notification.Message;
import org.example.notification.Notification;
import org.example.notification.NotificationPlan;
import org.example.notification.Recipient;
import org.example.notification.Sender;
import org.example.notification.application.Register;
import org.example.notification.mock.MemoryNotificationPlans;
import org.example.notification.mock.MemoryNotifications;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterTest {

    private MemoryNotifications notifications;
    private MemoryNotificationPlans scheduledNotifications;
    private Register register;

    @BeforeEach
    void init() {
        this.notifications = new MemoryNotifications();
        this.scheduledNotifications = new MemoryNotificationPlans();
        this.register = new RegistrationService(
                notifications,
                scheduledNotifications
        );
    }

    @Test
    void should_registration() {

        long beforeCount = notifications.count();
        Sender sender = Sender.of("senderId");
        Recipient recipient = Recipient.of("RecipientId", Channel.EMAIL);
        Message message = Message.of("title", "content");
        register.registration(sender, recipient, message);

        long afterCount = notifications.count();
        assertThat(afterCount).isEqualTo(beforeCount + 1);
    }

    @Test
    @DisplayName("즉시 발송을 요청하면 현재 시간으로 예약 발송 등록.")
    void should_registration_and_currentScheduled() {

        Sender sender = Sender.of("senderId");
        Recipient recipient = Recipient.of("RecipientId", Channel.EMAIL);
        Message message = Message.of("title", "content");
        register.registration(sender, recipient, message);

        Notification notification = notifications.findById(notifications.count()).orElseThrow();
        NotificationPlan notificationPlan = scheduledNotifications.findById(scheduledNotifications.count()).orElseThrow();

        assertThat(notificationPlan.getNotificationId()).isEqualTo(notification.getId());
        assertThat(notificationPlan.getScheduledAt()).isBefore(LocalDateTime.now());
    }

}
