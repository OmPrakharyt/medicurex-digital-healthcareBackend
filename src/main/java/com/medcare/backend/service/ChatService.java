package com.medcare.backend.service;

import com.medcare.backend.entity.Appointment;
import com.medcare.backend.entity.Chat;
import com.medcare.backend.repository.AppointmentRepository;
import com.medcare.backend.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public Chat sendMessage(Chat chat) {
        Appointment appointment = appointmentRepository.findById(chat.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (!"confirmed".equalsIgnoreCase(appointment.getStatus())) {
            throw new RuntimeException("Chat is only allowed for confirmed appointments");
        }

        chat.setTimestamp(LocalDateTime.now());
        return chatRepository.save(chat);
    }

    public List<Chat> getMessages(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (!"confirmed".equalsIgnoreCase(appointment.getStatus())) {
            throw new RuntimeException("Chat access restricted. Appointment status is " + appointment.getStatus());
        }

        return chatRepository.findByAppointmentIdOrderByTimestampAsc(appointmentId);
    }
}
