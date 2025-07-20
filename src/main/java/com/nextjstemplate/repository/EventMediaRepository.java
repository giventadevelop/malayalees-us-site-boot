package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventMedia;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import java.util.Optional;

/**
 * Spring Data JPA repository for the EventMedia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventMediaRepository extends JpaRepository<EventMedia, Long>, JpaSpecificationExecutor<EventMedia> {
  List<EventMedia> findByEventId(Long eventId);

  // Custom query to fetch EventMedia without LOB fields to avoid LOB stream
  // issues
  @Query(value = "SELECT id, tenant_id, title, event_media_type, storage_type, file_url, file_data_content_type, content_type, file_size, is_public, event_flyer, is_event_management_official_document, pre_signed_url, pre_signed_url_expires_at, alt_text, display_order, download_count, is_featured_video, featured_video_url, is_featured_image, is_hero_image, is_active_hero_image, created_at, updated_at, event_id, uploaded_by_id FROM event_media", nativeQuery = true)
  List<Object[]> findAllWithoutLobFieldsRaw();

  // Custom query to fetch EventMedia with specific criteria but without LOB
  // fields
  @Query("SELECT e FROM EventMedia e WHERE e.isFeaturedImage = :isFeaturedImage AND e.eventId = :eventId")
  List<EventMedia> findByIsFeaturedImageAndEventIdWithoutLobFields(
      @Param("isFeaturedImage") Boolean isFeaturedImage,
      @Param("eventId") Long eventId);

  /**
   * Update null LOB fields to prevent stream access errors from orphaned LOB
   * references
   */
  /*@Modifying
  @Transactional
  @Query("UPDATE EventMedia e SET e.description = NULL, e.fileData = NULL WHERE e.description IS NOT NULL OR e.fileData IS NOT NULL")
  int updateNullLobFields();*/
}
