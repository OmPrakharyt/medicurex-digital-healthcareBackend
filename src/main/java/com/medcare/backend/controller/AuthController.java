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

        user.setVerified(true); // auto verify

        User savedUser = userService.createUser(user);

        if(savedUser == null){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error","Email already registered"));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message","Registration successful"));
    }


    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String,String> request){
        return ResponseEntity.ok(Map.of("message","OTP disabled"));
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String,String> request){
        return ResponseEntity.ok(Map.of("message","Verified"));
    }


    @PostMapping("/login")
    @Operation(summary="Login user")
    public ResponseEntity<?> loginUser(
            @RequestBody Map<String,String> loginRequest){

        String email=loginRequest.get("email");
        String password=loginRequest.get("password");

        Optional<User> userOpt=userService.login(email,password);

        if(userOpt.isPresent()){
            return ResponseEntity.ok(userOpt.get()); // direct login no otp
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error","Invalid email or password"));
    }


    @PostMapping("/verify-login")
    public ResponseEntity<?> verifyLogin(
            @RequestBody Map<String,String> request){

        String email=request.get("email");

        Optional<User> userOpt=userService.getUserByEmail(email);

        if(userOpt.isPresent()){
            return ResponseEntity.ok(userOpt.get());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error","User not found"));
    }

}
