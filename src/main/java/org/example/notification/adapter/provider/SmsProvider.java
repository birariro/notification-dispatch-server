package org.example.notification.adapter.provider;

import org.example.notification.Channel;
import org.example.notification.Message;
import org.example.notification.Notification;
import org.example.notification.Recipient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
class SmsProvider extends AbstractDispatchMessageProvider {

    public SmsProvider(ProviderProperties properties, ApplicationEventPublisher publisher) {
        super(properties.sms(), publisher);
    }

    @Override
    public boolean support(Channel channel) {
        return Channel.SMS.equals(channel);
    }

    @Override
    protected Object buildRequestBody(Recipient recipient, Message message) {
        return new RequestBody(recipient.getId(), message.getTitle(), message.getContent());
    }

    private record RequestBody(String phoneNumber, String title, String contents) {
    }
}
