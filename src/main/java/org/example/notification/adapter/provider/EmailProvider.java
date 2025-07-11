package org.example.notification.adapter.provider;

import org.example.notification.Channel;
import org.example.notification.Message;
import org.example.notification.Recipient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
class EmailProvider extends AbstractDispatchMessageProvider {
    public EmailProvider(ProviderProperties properties, ApplicationEventPublisher publisher) {
        super(properties.email(), publisher);
    }

    @Override
    protected Object buildRequestBody(Recipient recipient, Message message) {
        return new RequestBody(recipient.getId(), message.getTitle(), message.getContent());
    }

    @Override
    public boolean support(Channel channel) {
        return Channel.EMAIL.equals(channel);
    }

    private record RequestBody(String emailAddress, String title, String contents) {
    }
}
