package org.example.notification.mock;

import org.example.notification.adapter.Notifications;
import org.example.notification.Notification;
import org.example.notification.Sender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryNotifications implements Notifications {
    Map<Long, Notification> memory = new HashMap<>();

    public long count() {
        return memory.size();
    }

    @Override
    public void save(Notification entity) {
        memory.put(count() + 1, entity);
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return Optional.ofNullable(memory.getOrDefault(id, null));
    }


    @Override
    public void completed(Long id) {

    }

    @Override
    public Iterable<Notification> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Notification> findAll(Pageable pageable) {
        return null;
    }
}
