package com.smartcampus.events.controller;

import com.smartcampus.events.entity.Event;
import com.smartcampus.events.service.EventService;
import com.smartcampus.events.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final EventService eventService;
    private final RegistrationService registrationService;

    @Autowired
    public AdminController(EventService eventService, RegistrationService registrationService) {
        this.eventService = eventService;
        this.registrationService = registrationService;
    }

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("listEvents", eventService.getAllEventsList());
        return "admin/dashboard";
    }

    @GetMapping("/showNewEventForm")
    public String showNewEventForm(Model model) {
        Event event = new Event();
        model.addAttribute("event", event);
        return "admin/event-form";
    }

    @PostMapping("/saveEvent")
    public String saveEvent(@Valid @ModelAttribute("event") Event event, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/event-form";
        }
        eventService.saveEvent(event);
        return "redirect:/admin";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") long id, Model model) {
        Event event = eventService.getEventById(id);
        model.addAttribute("event", event);
        return "admin/event-form";
    }

    @GetMapping("/deleteEvent/{id}")
    public String deleteEvent(@PathVariable(value = "id") long id) {
        this.eventService.deleteEventById(id);
        return "redirect:/admin";
    }

    @GetMapping("/registrations/{id}")
    public String viewRegistrations(@PathVariable(value = "id") long id, Model model) {
        Event event = eventService.getEventById(id);
        model.addAttribute("event", event);
        model.addAttribute("registrations", registrationService.getRegistrationsByEvent(id));
        model.addAttribute("totalCount", registrationService.getRegistrationCountForEvent(id));
        return "admin/registrations";
    }
}
