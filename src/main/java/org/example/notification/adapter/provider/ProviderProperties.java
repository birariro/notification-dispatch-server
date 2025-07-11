package org.example.notification.adapter.provider;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapter.api")
record ProviderProperties(String sms, String push, String email) {
}
