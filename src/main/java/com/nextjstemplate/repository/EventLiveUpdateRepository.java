package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventLiveUpdate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventLiveUpdate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventLiveUpdateRepository extends JpaRepository<EventLiveUpdate, Long>, JpaSpecificationExecutor<EventLiveUpdate> {}
