package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventAttendee;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventAttendee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventAttendeeRepository extends JpaRepository<EventAttendee, Long>, JpaSpecificationExecutor<EventAttendee> {}
