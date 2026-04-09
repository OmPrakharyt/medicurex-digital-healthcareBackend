package com.medcare.backend.service;

import com.medcare.backend.entity.DoctorProfile;
import com.medcare.backend.repository.DoctorProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorProfileService {

    @Autowired
    private DoctorProfileRepository doctorProfileRepository;

    public List<DoctorProfile> getAllDoctorProfiles() {
        return doctorProfileRepository.findAll();
    }

    public Optional<DoctorProfile> getDoctorProfileById(Long id) {
        return doctorProfileRepository.findById(id);
    }

    public Optional<DoctorProfile> getDoctorProfileByUserId(Long userId) {
        return doctorProfileRepository.findByUserId(userId);
    }

    public DoctorProfile createOrUpdateProfile(DoctorProfile profile) {
        // Check if profile already exists for this user
        Optional<DoctorProfile> existing = doctorProfileRepository.findByUserId(profile.getUserId());
        if (existing.isPresent()) {
            profile.setId(existing.get().getId());
        }
        return doctorProfileRepository.save(profile);
    }

    public boolean deleteProfile(Long id) {
        if (doctorProfileRepository.existsById(id)) {
            doctorProfileRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
