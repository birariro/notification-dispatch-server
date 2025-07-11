package org.example.notification.application.support;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.example.notification.Notification;
import org.example.notification.adapter.DispatchMessageProvider;
import org.example.notification.adapter.NotificationQueue;
import org.example.notification.adapter.RecoveryQueue;
import org.example.notification.adapter.WorkQueue;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
class NotificationSendProcessor {

    private final WorkQueue workQueue;
    private final RecoveryQueue recoveryQueue;
    private final List<DispatchMessageProvider> dispatchMessageProviders;

    private volatile boolean running = true;

    public NotificationSendProcessor(WorkQueue workQueue, RecoveryQueue recoveryQueue, List<DispatchMessageProvider> dispatchMessageProviders) {
        this.workQueue = workQueue;
        this.recoveryQueue = recoveryQueue;
        this.dispatchMessageProviders = dispatchMessageProviders;
    }

    @PostConstruct
    public void start() {
        startQueueThread(workQueue, "WorkQueue");
        startQueueThread(recoveryQueue, "RecoveryQueue");
    }

    private void startQueueThread(NotificationQueue queue, String name) {
        Thread.startVirtualThread(() -> {
            while (running) {
                try {
                    Notification notification = queue.poll();
                    handleNotification(notification);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.warn("virtual thread interrupted: [{}] ", name);
                } catch (Exception e) {
                    log.error("Error while processing notification: [{}] ", name, e);
                }
            }
        });
    }

    private void handleNotification(Notification notification) {
        Thread.startVirtualThread(() -> {
            try {
                DispatchMessageProvider provider = dispatchMessageProviders.stream()
                        .filter(p -> p.support(notification.getRecipient().getChannel()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException(
                                "No MessageProvider found for channel: " + notification.getRecipient().getChannel()));
                provider.dispatch(notification.getId(), notification.getRecipient(), notification.getMessage());
            } catch (Exception e) {
                log.error("Error sending notification: {}", notification.getId(), e);
            }
        });
    }

    @PreDestroy
    public void stop() {
        running = false;
    }
}
