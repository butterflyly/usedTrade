package com.example.usedTrade.Repository.Alram;

import com.example.usedTrade.Entity.Alarm.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification , Long> {
}
