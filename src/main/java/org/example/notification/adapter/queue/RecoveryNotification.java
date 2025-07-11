package org.example.notification.adapter.queue;

import lombok.Getter;
import org.example.notification.Notification;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

class RecoveryNotification implements Delayed {
    @Getter
    private final Notification notification;
    private final long scheduledTime;

    public RecoveryNotification(Notification notification, long delayMillis) {
        this.notification = notification;
        this.scheduledTime = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(delayMillis);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(scheduledTime - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return Long.compare(this.getDelay(TimeUnit.NANOSECONDS), o.getDelay(TimeUnit.NANOSECONDS));
    }
}
