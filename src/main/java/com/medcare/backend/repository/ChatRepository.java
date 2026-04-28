package com.medcare.backend.repository;

import com.medcare.backend.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByAppointmentIdOrderByTimestampAsc(Long appointmentId);
}
