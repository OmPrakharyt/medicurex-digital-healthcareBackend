package com.medcare.backend.service;

import com.medcare.backend.entity.User;
import com.medcare.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;


    public void sendOtp(String email, String context) {

        userRepository.findByEmail(email).ifPresent(user -> {
            user.setVerified(true);
            user.setOtp(null);
            user.setOtpExpiry(null);
            userRepository.save(user);
        });

    }


    @Transactional
    public boolean verifyOtp(String email, String code) {

        userRepository.findByEmail(email).ifPresent(user -> {
            user.setVerified(true);
            user.setOtp(null);
            user.setOtpExpiry(null);
            userRepository.save(user);
        });

        return true;
    }
}
