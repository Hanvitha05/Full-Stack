package com.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.academy.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}