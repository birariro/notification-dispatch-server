package org.example.notification.adapter;

import jakarta.transaction.Transactional;
import org.example.notification.Notification;
import org.example.notification.Sender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface Notifications extends PagingAndSortingRepository<Notification, Long> {

    void save(Notification entity);

    Optional<Notification> findById(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Notification e SET e.notifiedAt = CURRENT_TIMESTAMP WHERE e.id = :id")
    void completed(Long id);
}
