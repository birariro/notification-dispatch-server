package org.example.notification.adapter.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.notification.Channel;
import org.springframework.lang.Nullable;

record RegistrationRequest(
        @NotBlank
        String memberId,
        @NotBlank
        String recipientId,
        @NotBlank
        String title,
        @NotBlank
        String contents,
        @NotNull
        Channel channel,
        @Nullable
        String scheduledDate
) {
}
