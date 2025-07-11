package org.example.notification;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sender {

    @Column(name = "sender_id", nullable = false)
    private String id;

    private Sender(String id) {
        this.id = id;
    }

    public static Sender of(String id) {
        return new Sender(id);
    }
}
