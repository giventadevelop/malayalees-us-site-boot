package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventCalendarEntry;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventCalendarEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventCalendarEntryRepository
    extends JpaRepository<EventCalendarEntry, Long>, JpaSpecificationExecutor<EventCalendarEntry> {}
