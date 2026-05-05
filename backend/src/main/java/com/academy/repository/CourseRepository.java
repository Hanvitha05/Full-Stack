package com.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.academy.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}