package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventScoreCard;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventScoreCard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventScoreCardRepository extends JpaRepository<EventScoreCard, Long>, JpaSpecificationExecutor<EventScoreCard> {}
