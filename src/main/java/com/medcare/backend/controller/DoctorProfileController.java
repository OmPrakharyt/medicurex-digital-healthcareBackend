package com.medcare.backend.controller;

import com.medcare.backend.entity.DoctorProfile;
import com.medcare.backend.service.DoctorProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/doctors")
@Tag(name = "Doctor Profile Controller", description = "Doctor profile management APIs")
public class DoctorProfileController {

    @Autowired
    private DoctorProfileService doctorProfileService;

    @GetMapping
    @Operation(summary = "Get all doctor profiles")
    public List<DoctorProfile> getAllDoctorProfiles() {
        return doctorProfileService.getAllDoctorProfiles();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get doctor profile by ID")
    public ResponseEntity<DoctorProfile> getDoctorProfileById(@PathVariable Long id) {
        Optional<DoctorProfile> profile = doctorProfileService.getDoctorProfileById(id);
        return profile.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get doctor profile by user ID")
    public ResponseEntity<DoctorProfile> getDoctorProfileByUserId(@PathVariable Long userId) {
        Optional<DoctorProfile> profile = doctorProfileService.getDoctorProfileByUserId(userId);
        return profile.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    @Operation(summary = "Create or update doctor profile")
    public ResponseEntity<DoctorProfile> createOrUpdateProfile(@RequestBody DoctorProfile profile) {
        DoctorProfile savedProfile = doctorProfileService.createOrUpdateProfile(profile);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProfile);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete doctor profile")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        if (doctorProfileService.deleteProfile(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
