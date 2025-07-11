package org.example.notification.mock;

import org.example.notification.adapter.NotificationPlans;
import org.example.notification.NotificationPlan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MemoryNotificationPlans implements NotificationPlans {
    Map<Long, NotificationPlan> memory = new HashMap<>();

    public long count() {
        return memory.size();
    }

    @Override
    public void save(NotificationPlan entity) {
        memory.put(count() + 1, entity);
    }

    @Override
    public List<NotificationPlan> findDueNotifications() {
        return List.of();
    }

    @Override
    public void completed(Long id) {

    }

    public Optional<NotificationPlan> findById(Long id) {
        return Optional.ofNullable(memory.getOrDefault(id, null));
    }
}
