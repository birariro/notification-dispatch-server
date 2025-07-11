package org.example.notification;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Embedded
    private Sender sender;

    @Embedded
    @Getter
    private Recipient recipient;

    @Embedded
    @Getter
    private Message message;

    @Column(name = "notified_at")
    @Getter
    private LocalDateTime notifiedAt;

    private Notification(Sender sender, Recipient recipient, Message message) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
    }

    public static Notification of(Sender sender, Recipient recipient, Message message) {
        return new Notification(sender, recipient, message);
    }
}
