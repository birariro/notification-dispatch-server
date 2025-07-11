package org.example.notification.adapter;

import jakarta.transaction.Transactional;
import org.example.notification.NotificationPlan;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface NotificationPlans extends Repository<NotificationPlan, Long> {

    void save(NotificationPlan entity);

    @Query("SELECT s FROM NotificationPlan s WHERE s.stats = 'WAITING' AND s.scheduledAt <= CURRENT_TIMESTAMP")
    List<NotificationPlan> findDueNotifications();

    @Transactional
    @Modifying
    @Query("UPDATE NotificationPlan e SET e.stats = 'COMPLETED' WHERE e.id = :id")
    void completed(Long id);

}
