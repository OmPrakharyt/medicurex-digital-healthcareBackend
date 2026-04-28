package com.medcare.backend.controller;

import com.medcare.backend.entity.Chat;
import com.medcare.backend.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@Tag(name = "Chat Controller", description = "Messaging APIs for Patients and Doctors")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/send")
    @Operation(summary = "Send a message")
    public ResponseEntity<Chat> sendMessage(@RequestBody Chat chat) {
        return ResponseEntity.ok(chatService.sendMessage(chat));
    }

    @GetMapping("/{appointmentId}")
    @Operation(summary = "Get messages for an appointment")
    public ResponseEntity<List<Chat>> getMessages(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(chatService.getMessages(appointmentId));
    }
}
