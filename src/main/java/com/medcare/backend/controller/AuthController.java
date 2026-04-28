package com.medcare.backend.controller;

import com.medcare.backend.entity.User;
import com.medcare.backend.service.UserService;
import com.medcare.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth Controller", description = "Authentication and Registration APIs")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        if (savedUser == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Email already registered"));
        }
        authService.sendOtp(user.getEmail(), "REGISTER");
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Registration successful. Please verify OTP sent to your email."));
    }

    @PostMapping("/send-otp")
    @Operation(summary = "Resend OTP to email")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        // Resend uses REGISTER context for same welcome message or generic if preferred. Using REGISTER.
        authService.sendOtp(email, "REGISTER");
        return ResponseEntity.ok(Map.of("message", "OTP sent successfully"));
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Verify email with OTP")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        if (authService.verifyOtp(email, otp)) {
            return ResponseEntity.ok(Map.of("message", "Email verified successfully"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid or expired OTP"));
    }

    @PostMapping("/login")
    @Operation(summary = "Login user")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        Optional<User> userOpt = userService.login(email, password);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (!Boolean.TRUE.equals(user.getVerified())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Your account is not verified. Please verify your email first."));
            }
            
            // Generate OTP for MFA
            authService.sendOtp(email, "LOGIN");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of(
                "message", "MFA required", 
                "mfaRequired", "true",
                "email", email
            ));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid email or password"));
    }

    @PostMapping("/verify-login")
    @Operation(summary = "Verify login with OTP for MFA")
    public ResponseEntity<?> verifyLogin(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        if (authService.verifyOtp(email, otp)) {
            Optional<User> userOpt = userService.getUserByEmail(email);
            if (userOpt.isPresent()) {
                return ResponseEntity.ok(userOpt.get());
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid or expired OTP"));
    }
}
