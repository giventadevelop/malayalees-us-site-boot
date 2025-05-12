package com.nextjstemplate.repository;

import com.nextjstemplate.domain.CalendarEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CalendarEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {}
