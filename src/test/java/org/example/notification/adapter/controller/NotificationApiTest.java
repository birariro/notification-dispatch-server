package org.example.notification.adapter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.notification.Message;
import org.example.notification.Notification;
import org.example.notification.Recipient;
import org.example.notification.Sender;
import org.example.notification.application.Register;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = NotificationApi.class)
@Import(NotificationApiTest.TestConfig.class)
class NotificationApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("scheduledDate nullable")
    void should_scheduledDate_nullable() throws Exception {

        mockMvc.perform(post("/notification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(
                        Map.of(
                                "memberId", "id",
                                "recipientId", "id",
                                "title", "title",
                                "contents", "content",
                                "channel", "PUSH",
                                "scheduledDate", ""
                        ))
                )).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("잘못된 scheduledDate 포멧은 400에러")
    void should_BadRequest_when_nonFormatScheduledDate() throws Exception {

        mockMvc.perform(post("/notification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(
                        Map.of(
                                "memberId", "id",
                                "recipientId", "id",
                                "title", "title",
                                "contents", "content",
                                "channel", "PUSH",
                                "scheduledDate", "20251102"
                        ))
                )).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("존재하지 않는 Channel 요청시 실패")
    void should_BadRequest_when_nonExistChannel() throws Exception {

        mockMvc.perform(post("/notification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(
                        Map.of(
                                "memberId", "id",
                                "recipientId", "id",
                                "title", "title",
                                "contents", "content",
                                "channel", "NOTCHANNEL",
                                "scheduledDate", "202511230707"
                        ))
                )).andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("scheduledDate를 제외한 나머지 필드는 반드시 필요")
    void should_BadRequest_when_blankField() throws Exception {

        mockMvc.perform(post("/notification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(
                        Map.of(
                                "memberId", "",
                                "recipientId", "id",
                                "title", "title",
                                "contents", "content",
                                "channel", "EMAIL"
                        ))
                )).andExpect(status().isBadRequest());
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public Register register() {
            return new Register() {
                @Override
                public void registration(Sender sender, Recipient recipient, Message message) {

                }

                @Override
                public void reservation(Sender sender, Recipient recipient, Message message, LocalDateTime sendDesiredAt) {

                }
            };
        }
    }
}
