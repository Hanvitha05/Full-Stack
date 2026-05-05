package com.academy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.academy.entity.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByEmail(String email);
}
