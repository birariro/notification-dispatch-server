package org.example.notification.adapter.provider;

import lombok.extern.slf4j.Slf4j;
import org.example.notification.CompletedEvent;
import org.example.notification.FailedEvent;
import org.example.notification.Message;
import org.example.notification.Recipient;
import org.example.notification.adapter.DispatchMessageProvider;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

@Slf4j
abstract class AbstractDispatchMessageProvider implements DispatchMessageProvider {

    private final RestClient restClient;
    private final ApplicationEventPublisher publisher;

    protected AbstractDispatchMessageProvider(String baseUrl, ApplicationEventPublisher publisher) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
        this.publisher = publisher;
    }

    @Override
    public void dispatch(Long id, Recipient recipient, Message message) {
        log.info("Sending message id: {}", id);

        try {
            Object requestBody = buildRequestBody(recipient, message);
            ResponseEntity<ResponseBody> response = sendRequest(requestBody);

            if (isSuccessful(response)) {
                publisher.publishEvent(new CompletedEvent(id));
            } else {
                publisher.publishEvent(new FailedEvent(id));
            }

        } catch (RuntimeException e) {
            log.error("Failed to send message: {}", e.getMessage());
            publisher.publishEvent(new FailedEvent(id));
        }
    }

    private ResponseEntity<ResponseBody> sendRequest(Object requestBody) {
        return restClient.post()
                .body(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {

                })
                .toEntity(ResponseBody.class);
    }

    private boolean isSuccessful(ResponseEntity<ResponseBody> response) {
        return response.getStatusCode().is2xxSuccessful();
    }

    protected abstract Object buildRequestBody(Recipient recipient, Message message);

    record ResponseBody(String message) {

    }
}
