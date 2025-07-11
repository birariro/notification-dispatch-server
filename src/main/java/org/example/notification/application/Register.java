package org.example.notification.application;

import org.example.notification.Message;
import org.example.notification.Recipient;
import org.example.notification.Sender;

import java.time.LocalDateTime;

public interface Register {
    /**
     * 알림 발송 요청
     */
    void registration(Sender sender, Recipient recipient, Message message);

    /**
     * 알림 예약 발송 요청
     */
    void reservation(Sender sender, Recipient recipient, Message message, LocalDateTime sendDesiredAt);
}
