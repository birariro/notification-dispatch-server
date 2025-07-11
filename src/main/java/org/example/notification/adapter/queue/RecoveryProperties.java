package org.example.notification.adapter.queue;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapter.queue.recovery")
record RecoveryProperties(
        long delay,
        int maxDelay
) {
}
