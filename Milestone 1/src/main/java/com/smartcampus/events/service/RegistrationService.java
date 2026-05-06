package com.smartcampus.events.service;

import com.smartcampus.events.entity.Event;
import com.smartcampus.events.entity.Registration;
import com.smartcampus.events.repository.EventRepository;
import com.smartcampus.events.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);

    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final EmailService emailService;

    @Autowired
    public RegistrationService(RegistrationRepository registrationRepository, EventRepository eventRepository, EmailService emailService) {
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
        this.emailService = emailService;
    }

    public Registration registerForEvent(Long eventId, String studentName, String studentEmail) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Optional<Registration> existingRegistration = registrationRepository.findByStudentEmailAndEventId(studentEmail, eventId);
        if (existingRegistration.isPresent() && "VERIFIED".equals(existingRegistration.get().getStatus())) {
            throw new RuntimeException("You are already registered for this event.");
        } else if (existingRegistration.isPresent() && "PENDING".equals(existingRegistration.get().getStatus())) {
            // Re-use the existing pending registration
            Registration reg = existingRegistration.get();
            reg.setStudentName(studentName);
            generateAndSetOtp(reg);
            return registrationRepository.save(reg);
        }

        Registration registration = new Registration(studentName, studentEmail, event);
        generateAndSetOtp(registration);
        return registrationRepository.save(registration);
    }

    private void generateAndSetOtp(Registration registration) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        registration.setOtp(otp);
        registration.setOtpExpiry(java.time.LocalDateTime.now().plusMinutes(10));
        registration.setStatus("PENDING");
        
        // Simulate sending email in console
        logger.info("======================================================");
        logger.info("MOCK EMAIL LOGGED TO: {}", registration.getStudentEmail());
        logger.info("YOUR OTP FOR EVENT '{}' IS: {}", registration.getEvent().getTitle(), otp);
        logger.info("======================================================");

        try {
            // Send real email
            emailService.sendOtpEmail(registration.getStudentEmail(), otp, registration.getEvent().getTitle());
        } catch (Exception e) {
            logger.error("Failed to send OTP email: {}", e.getMessage());
            // We still log the OTP above, so developers can test without valid SMTP credentials
            // In a production app, you might want to rethrow this.
        }
    }

    public boolean verifyOtp(Long registrationId, String inputOtp) {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        if ("VERIFIED".equals(registration.getStatus())) {
            throw new RuntimeException("Registration is already verified.");
        }

        if (registration.getOtpExpiry().isBefore(java.time.LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired. Please register again.");
        }

        if (registration.getOtp().equals(inputOtp)) {
            registration.setStatus("VERIFIED");
            registrationRepository.save(registration);
            return true;
        }
        
        return false;
    }

    public List<Registration> getRegistrationsByEvent(Long eventId) {
        return registrationRepository.findByEventId(eventId);
    }

    public List<Registration> getRegistrationsByEmail(String email) {
        return registrationRepository.findByStudentEmailOrderByRegistrationDateDesc(email).stream()
                .filter(reg -> "VERIFIED".equals(reg.getStatus()))
                .toList();
    }

    public long getRegistrationCountForEvent(Long eventId) {
        return registrationRepository.countByEventId(eventId);
    }
}
