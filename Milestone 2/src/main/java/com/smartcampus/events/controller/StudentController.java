package com.smartcampus.events.controller;

import com.smartcampus.events.entity.Event;
import com.smartcampus.events.service.EventService;
import com.smartcampus.events.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class StudentController {

    private final EventService eventService;
    private final RegistrationService registrationService;

    @Autowired
    public StudentController(EventService eventService, RegistrationService registrationService) {
        this.eventService = eventService;
        this.registrationService = registrationService;
    }

    @GetMapping("/")
    public String landingPage() {
        return "landing";
    }

    @GetMapping("/student/events")
    public String viewHomePage(Model model,
                               @RequestParam(defaultValue = "1") int pageNo,
                               @RequestParam(defaultValue = "date") String sortField,
                               @RequestParam(defaultValue = "asc") String sortDir,
                               @RequestParam(required = false) String department,
                               @RequestParam(required = false) String eventType) {
        int pageSize = 6;
        Page<Event> page;
        
        if (department != null && !department.isEmpty() || eventType != null && !eventType.isEmpty()) {
            page = eventService.filterEvents(null, department.isEmpty() ? null : department, eventType.isEmpty() ? null : eventType, pageNo, pageSize, sortField, sortDir);
        } else {
            page = eventService.filterEvents(null, null, null, pageNo, pageSize, sortField, sortDir);
        }

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("listEvents", page.getContent());
        
        model.addAttribute("department", department);
        model.addAttribute("eventType", eventType);

        return "index";
    }

    @GetMapping("/student/event/{id}")
    public String showEventDetails(@PathVariable(value = "id") long id, Model model) {
        Event event = eventService.getEventById(id);
        model.addAttribute("event", event);
        return "event-details";
    }

    @GetMapping("/student/register/{id}")
    public String showRegistrationForm(@PathVariable(value = "id") long id, Model model) {
        Event event = eventService.getEventById(id);
        model.addAttribute("event", event);
        return "registration";
    }

    @PostMapping("/student/register")
    public String registerForEvent(@RequestParam("eventId") Long eventId,
                                   @RequestParam("studentName") String studentName,
                                   @RequestParam("studentEmail") String studentEmail,
                                   Model model) {
        try {
            com.smartcampus.events.entity.Registration registration = registrationService.registerForEvent(eventId, studentName, studentEmail);
            return "redirect:/student/verify-otp/" + registration.getId();
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            Event event = eventService.getEventById(eventId);
            model.addAttribute("event", event);
            return "registration";
        }
    }

    @GetMapping("/student/verify-otp/{id}")
    public String showOtpForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("registrationId", id);
        return "verify-otp";
    }

    @PostMapping("/student/verify-otp")
    public String verifyOtp(@RequestParam("registrationId") Long registrationId,
                            @RequestParam("otp") String otp,
                            Model model) {
        try {
            boolean isVerified = registrationService.verifyOtp(registrationId, otp);
            if (isVerified) {
                model.addAttribute("message", "Successfully verified and registered for the event!");
                model.addAttribute("registrationId", registrationId);
                return "registration-success";
            } else {
                model.addAttribute("error", "Invalid OTP. Please try again.");
                model.addAttribute("registrationId", registrationId);
                return "verify-otp";
            }
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("registrationId", registrationId);
            return "verify-otp";
        }
    }

    @GetMapping("/student/my-registrations")
    public String viewMyRegistrationsForm() {
        return "my-registrations-form";
    }

    @PostMapping("/student/my-registrations")
    public String viewMyRegistrations(@RequestParam("email") String email, Model model) {
        model.addAttribute("registrations", registrationService.getRegistrationsByEmail(email));
        model.addAttribute("email", email);
        return "my-registrations";
    }
}
