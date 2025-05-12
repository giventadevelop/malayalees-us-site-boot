package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventMedia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventMedia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventMediaRepository extends JpaRepository<EventMedia, Long> {}
