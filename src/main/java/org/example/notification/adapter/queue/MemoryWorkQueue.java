package org.example.notification.adapter.queue;

import lombok.extern.slf4j.Slf4j;
import org.example.notification.Notification;
import org.example.notification.adapter.Notifications;
import org.example.notification.adapter.WorkQueue;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Component
class MemoryWorkQueue implements WorkQueue {

    private final BlockingQueue<Notification> queue;
    private final Notifications notifications;

    public MemoryWorkQueue(Notifications notifications) {
        this.queue = new LinkedBlockingQueue<>();
        this.notifications = notifications;
    }

    @Override
    public Notification poll() throws InterruptedException {
        Notification notification = queue.take();
        log.info("[Work] Queue take id : {}", notification.getId());
        return notification;
    }

    @Override
    public void add(Long id) {

        log.info("[Work] Queue size: {}, new element[id:{}]", queue.size(), id);
        Notification notification = notifications.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        queue.offer(notification);
    }

}
