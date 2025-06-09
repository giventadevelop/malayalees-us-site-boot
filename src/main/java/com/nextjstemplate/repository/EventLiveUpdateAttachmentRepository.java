package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventLiveUpdateAttachment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventLiveUpdateAttachment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventLiveUpdateAttachmentRepository
    extends JpaRepository<EventLiveUpdateAttachment, Long>, JpaSpecificationExecutor<EventLiveUpdateAttachment> {}
