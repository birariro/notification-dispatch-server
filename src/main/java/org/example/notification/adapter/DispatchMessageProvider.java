package org.example.notification.adapter;

import org.example.notification.Channel;
import org.example.notification.Message;
import org.example.notification.Recipient;

public interface DispatchMessageProvider {

    /**
     * 알림 전송
     *
     * @param id        식별 값
     * @param recipient 수신자 정보
     * @param message   메시지
     */
    void dispatch(Long id, Recipient recipient, Message message);

    boolean support(Channel channel);
}
