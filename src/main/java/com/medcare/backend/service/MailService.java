package com.medcare.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        if (mailSender == null) {
            System.out.println("MailSender not configured. Printing to log:");
            System.out.println("To: " + to);
            System.out.println("Subject: " + subject);
            System.out.println("Body: " + body);
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("MedCare <YOUR_GMAIL_ADDRESS@gmail.com>");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            System.out.println("Email sent successfully to " + to);
        } catch (Exception e) {
            System.err.println("CRITICAL: Failed to send email to " + to);
            System.err.println("Error details: " + e.getMessage());
            // Log the stack trace if needed for deeper debugging
            // e.printStackTrace();
        }
    }
}
