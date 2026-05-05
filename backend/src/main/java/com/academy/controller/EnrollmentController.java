package com.academy.controller;

import com.academy.entity.Enrollment;
import com.academy.repository.EnrollmentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enroll")
@CrossOrigin("*")
public class EnrollmentController {

    private final EnrollmentRepository repo;

    public EnrollmentController(EnrollmentRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Enrollment enroll(@RequestBody Enrollment e) {
        return repo.save(e);
    }

    @GetMapping("/{email}")
    public List<Enrollment> getUserCourses(@PathVariable String email) {
        return repo.findByEmail(email);
    }
}
