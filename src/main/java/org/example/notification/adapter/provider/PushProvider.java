package org.example.notification.adapter.provider;

import org.example.notification.Channel;
import org.example.notification.Message;
import org.example.notification.Recipient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
class PushProvider extends AbstractDispatchMessageProvider {

    public PushProvider(ProviderProperties properties, ApplicationEventPublisher publisher) {
        super(properties.push(), publisher);
    }

    @Override
    public boolean support(Channel channel) {
        return Channel.PUSH.equals(channel);
    }

    @Override
    protected Object buildRequestBody(Recipient recipient, Message message) {
        return new RequestBody(recipient.getId(), message.getTitle(), message.getContent());
    }

    private record RequestBody(String talkId, String title, String contents) {
    }
}
