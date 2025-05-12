package com.nextjstemplate.repository;

import com.nextjstemplate.domain.PollResponse;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PollResponse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PollResponseRepository extends JpaRepository<PollResponse, Long> {}
