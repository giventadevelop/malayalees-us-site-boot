package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventOrganizer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventOrganizer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventOrganizerRepository extends JpaRepository<EventOrganizer, Long> {}
