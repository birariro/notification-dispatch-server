package org.example.notification;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@ToString
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {

    @Getter
    @Column(name = "title", nullable = false)
    private String title;

    @Getter
    @Column(name = "content", nullable = false)
    private String content;

    private Message(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static Message of(String title, String content) {
        return new Message(title, content);
    }
}
