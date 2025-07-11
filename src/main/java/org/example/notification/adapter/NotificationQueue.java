package org.example.notification.adapter;

import org.example.notification.Notification;

public interface NotificationQueue {
    /**
     * 대기열에서 처리해야할 Notification 을 얻는다
     */
    Notification poll() throws InterruptedException;

    /**
     * 대기열에 Notification 을 추가한다
     */
    void add(Long id);
}
