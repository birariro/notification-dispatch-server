package org.example.notification.adapter.queue;

import lombok.extern.slf4j.Slf4j;
import org.example.notification.Notification;
import org.example.notification.adapter.Notifications;
import org.example.notification.adapter.RecoveryQueue;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

@Slf4j
@Component
class MemoryRecoveryQueue implements RecoveryQueue {

    private final DelayQueue<RecoveryNotification> queue;
    private final ConcurrentHashMap<Long, Integer> retryCounts = new ConcurrentHashMap<>();
    private final Notifications notifications;
    private final RecoveryProperties properties;

    public MemoryRecoveryQueue(Notifications notifications, RecoveryProperties properties) {
        this.queue = new DelayQueue<>();
        this.notifications = notifications;
        this.properties = properties;
    }

    @Override
    public Notification poll() throws InterruptedException {
        RecoveryNotification recoveryNotification = queue.take();
        Notification notification = recoveryNotification.getNotification();
        log.info("[Recovery] Queue take id : {}", notification.getId());
        return notification;
    }

    private long getBackoffDelay(Long id) {

        int retryCount = retryCounts.getOrDefault(id, 0);
        long exponentialDelay = (long) (properties.delay() * Math.pow(2, retryCount));
        retryCounts.put(id, retryCount + 1);

        return Math.min(exponentialDelay, properties.maxDelay());
    }

    @Override
    public void add(Long id) {

        long backoffDelay = getBackoffDelay(id);
        log.info("[Recovery] Queue size: {}, new element[id:{}, delay:{}]", queue.size(), id, backoffDelay);

        Notification notification = notifications.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        queue.offer(new RecoveryNotification(notification, backoffDelay));
    }

    @Override
    public void resetRetry(Long id) {
        retryCounts.remove(id);
    }
}
