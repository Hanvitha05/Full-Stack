package com.smartcampus.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendOtpEmail(String to, String otp, String eventTitle) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("smartcampus.events@gmail.com"); // Can be overridden by spring.mail.username
        message.setTo(to);
        message.setSubject("Your OTP for Event Registration: " + eventTitle);
        message.setText("Hello,\n\n" +
                "You are attempting to register for the event: " + eventTitle + ".\n\n" +
                "Your One-Time Password (OTP) is: " + otp + "\n\n" +
                "This OTP will expire in 10 minutes.\n\n" +
                "If you did not request this, please ignore this email.\n\n" +
                "Best regards,\nSmart Campus Events Team");
        
        emailSender.send(message);
    }
}
