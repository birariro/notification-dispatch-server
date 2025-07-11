package org.example.notification.application.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.notification.NotificationPlan;
import org.example.notification.adapter.NotificationPlans;
import org.example.notification.adapter.WorkQueue;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
class AppendQueueScheduler {

    private final WorkQueue workQueue;
    private final NotificationPlans notificationPlans;

    /**
     * 예약 발송일에 도달한 Notification을 workQueue에 추가
     */
    @Scheduled(fixedDelayString = "${application.scheduler.planDelay}")
    void AddScheduledNotifications() {
        List<NotificationPlan> dueNotifications = notificationPlans.findDueNotifications();
        if (dueNotifications.isEmpty()) {
            return;
        }

        log.info("due notifications size: {}", dueNotifications.size());
        for (NotificationPlan dueNotification : dueNotifications) {
            workQueue.add(dueNotification.getNotificationId());
            notificationPlans.completed(dueNotification.getId());
        }
    }
}
