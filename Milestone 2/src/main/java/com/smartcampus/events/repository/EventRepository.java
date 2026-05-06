package com.smartcampus.events.repository;

import com.smartcampus.events.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE " +
           "(:date IS NULL OR e.date >= :date) AND " +
           "(:department IS NULL OR e.department = :department) AND " +
           "(:eventType IS NULL OR e.eventType = :eventType)")
    Page<Event> findByFilters(@Param("date") LocalDateTime date, 
                              @Param("department") String department, 
                              @Param("eventType") String eventType, 
                              Pageable pageable);
}
