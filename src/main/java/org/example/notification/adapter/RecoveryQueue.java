package org.example.notification.adapter;

public interface RecoveryQueue extends NotificationQueue {
    void resetRetry(Long id);
}
