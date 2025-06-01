package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventPoll;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventPoll entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventPollRepository extends JpaRepository<EventPoll, Long>, JpaSpecificationExecutor<EventPoll> {}
