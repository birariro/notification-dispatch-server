package org.example.notification.application.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.notification.CompletedEvent;
import org.example.notification.FailedEvent;
import org.example.notification.adapter.Notifications;
import org.example.notification.adapter.RecoveryQueue;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class AppendQueueEventListener {

    private final RecoveryQueue recoveryQueue;
    private final Notifications notifications;

    /**
     * send 실패 메시지를 수신시 재 요청 하도록 복구 큐에 추가
     */
    @EventListener
    void onFailedEvent(FailedEvent e) {
        log.info("Failed id : {}", e.id());
        recoveryQueue.add(e.id());
    }

    /**
     * send 성공 메시지를 수신시 notification를 완료 처리 및 복구 지수 백오프 초기화
     */
    @EventListener
    void onCompletedEvent(CompletedEvent e) {
        log.info("Completed id : {}", e.id());
        notifications.completed(e.id());
        recoveryQueue.resetRetry(e.id());
    }
}
