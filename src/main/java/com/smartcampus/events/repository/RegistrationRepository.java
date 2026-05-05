package com.smartcampus.events.repository;

import com.smartcampus.events.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByEventId(Long eventId);
    List<Registration> findByStudentEmailOrderByRegistrationDateDesc(String email);
    Optional<Registration> findByStudentEmailAndEventId(String email, Long eventId);
    long countByEventId(Long eventId);
}
