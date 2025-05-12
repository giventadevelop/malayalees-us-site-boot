package com.nextjstemplate.repository;

import com.nextjstemplate.domain.Poll;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Poll entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {}
