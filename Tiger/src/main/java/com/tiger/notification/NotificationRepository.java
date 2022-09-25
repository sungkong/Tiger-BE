package com.tiger.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select n from Notification n where n.receiver.id = :memberId order by n.id desc")
    List<Notification> findAllByMemberId(@Param("memberId") Long memberId);

    @Query("select count(n) from Notification n where n.receiver.id = :memberId and n.isRead = false")
    Long countUnReadNotifications(@Param("memberId") Long memberId);

    Optional<Notification> findById(Long NotificationsId);

    void deleteAllByReceiverId(Long receiverId);
    void deleteById(Long notificationId);

}
