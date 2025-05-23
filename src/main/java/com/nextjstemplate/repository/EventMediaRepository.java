package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventMedia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Spring Data JPA repository for the EventMedia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventMediaRepository extends JpaRepository<EventMedia, Long>, JpaSpecificationExecutor<EventMedia> {
  List<EventMedia> findByEvent_Id(Long eventId);

}
