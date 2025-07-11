package org.example.notification;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(name = "notification_id", nullable = false)
    @Getter
    private Long notificationId;

    @Column(name = "scheduled_At")
    @Getter
    private LocalDateTime scheduledAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "stats", nullable = false)
    private Stats stats;

    private NotificationPlan(Long notificationId, LocalDateTime scheduledAt, Stats stats) {
        this.notificationId = notificationId;
        this.scheduledAt = scheduledAt;
        this.stats = stats;
    }

    public static NotificationPlan of(Notification notification, LocalDateTime scheduledAt) {
        return new NotificationPlan(notification.getId(), scheduledAt, Stats.WAITING);
    }

}
