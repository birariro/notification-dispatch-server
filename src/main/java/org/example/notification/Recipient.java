package org.example.notification;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@ToString
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recipient {

    @Column(name = "recipient_id", nullable = false)
    @Getter private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false)
    @Getter private Channel channel;

    private Recipient(String id, Channel channel) {
        this.id = id;
        this.channel = channel;
    }

    public static Recipient of(String id, Channel channel) {
        return new Recipient(id, channel);
    }
}
