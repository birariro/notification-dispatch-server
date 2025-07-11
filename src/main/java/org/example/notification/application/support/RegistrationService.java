package org.example.notification.application.support;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.notification.*;
import org.example.notification.adapter.NotificationPlans;
import org.example.notification.adapter.Notifications;
import org.example.notification.application.Register;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Transactional
@RequiredArgsConstructor
class RegistrationService implements Register {

    private final Notifications notifications;
    private final NotificationPlans notificationPlans;

    @Override
    public void registration(Sender sender, Recipient recipient, Message message) {
        reservation(sender, recipient, message, LocalDateTime.now());
    }

    @Override
    public void reservation(Sender sender, Recipient recipient, Message message, LocalDateTime sendDesiredAt) {

        Notification notification = Notification.of(sender, recipient, message);
        notifications.save(notification);

        NotificationPlan notificationPlan = NotificationPlan.of(notification, sendDesiredAt);
        notificationPlans.save(notificationPlan);
    }
}
