package com.nextjstemplate.repository;

import com.nextjstemplate.domain.PollOption;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PollOption entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PollOptionRepository extends JpaRepository<PollOption, Long>, JpaSpecificationExecutor<PollOption> {}
