package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventPollOption;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventPollOption entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventPollOptionRepository extends JpaRepository<EventPollOption, Long>, JpaSpecificationExecutor<EventPollOption> {}
