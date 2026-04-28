package com.medcare.backend.service;

import com.medcare.backend.entity.User;
import com.medcare.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

    public void sendOtp(String email, String context) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return;

        User user = userOpt.get();
        String code = String.format("%06d", new Random().nextInt(999999));
        
        user.setOtp(code);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);

        String subject = "MedCare Verification OTP";
        String body = "Your OTP is: " + code + ".\nIt expires in 5 minutes.";

        if ("REGISTER".equals(context)) {
            subject = "Welcome to MediCurex - Verification OTP";
            body = "Hey welcome on medicurx!\n\nYour OTP for account verification is: " + code + ".\nIt expires in 5 minutes.";
        } else if ("LOGIN".equals(context)) {
            subject = "MediCurex - Login MFA OTP";
            body = "Your OTP for login is: " + code + ".\nIt expires in 5 minutes.";
        }

        mailService.sendEmail(email, subject, body);
    }

    @Transactional
    public boolean verifyOtp(String email, String code) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getOtp() != null && 
                user.getOtp().equals(code) && 
                user.getOtpExpiry() != null && 
                user.getOtpExpiry().isAfter(LocalDateTime.now())) {
                
                user.setVerified(true);
                user.setOtp(null); // Clear OTP after success
                user.setOtpExpiry(null);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
}
