package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventDetailsRepository extends JpaRepository<EventDetails, Long>, JpaSpecificationExecutor<EventDetails> {}
