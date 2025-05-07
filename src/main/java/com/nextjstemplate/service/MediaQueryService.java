package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.Media;
import com.nextjstemplate.repository.MediaRepository;
import com.nextjstemplate.service.criteria.MediaCriteria;
import com.nextjstemplate.service.dto.MediaDTO;
import com.nextjstemplate.service.mapper.MediaMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Media} entities in the database.
 * The main input is a {@link MediaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MediaDTO} or a {@link Page} of {@link MediaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MediaQueryService extends QueryService<Media> {

    private final Logger log = LoggerFactory.getLogger(MediaQueryService.class);

    private final MediaRepository mediaRepository;

    private final MediaMapper mediaMapper;

    public MediaQueryService(MediaRepository mediaRepository, MediaMapper mediaMapper) {
        this.mediaRepository = mediaRepository;
        this.mediaMapper = mediaMapper;
    }

    /**
     * Return a {@link List} of {@link MediaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MediaDTO> findByCriteria(MediaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Media> specification = createSpecification(criteria);
        return mediaMapper.toDto(mediaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MediaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MediaDTO> findByCriteria(MediaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Media> specification = createSpecification(criteria);
        return mediaRepository.findAll(specification, page).map(mediaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MediaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Media> specification = createSpecification(criteria);
        return mediaRepository.count(specification);
    }

    /**
     * Function to convert {@link MediaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Media> createSpecification(MediaCriteria criteria) {
        Specification<Media> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Media_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Media_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Media_.description));
            }
            if (criteria.getMediaType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMediaType(), Media_.mediaType));
            }
            if (criteria.getStorageType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStorageType(), Media_.storageType));
            }
            if (criteria.getFileUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileUrl(), Media_.fileUrl));
            }
            if (criteria.getContentType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContentType(), Media_.contentType));
            }
            if (criteria.getFileSize() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFileSize(), Media_.fileSize));
            }
            if (criteria.getIsPublic() != null) {
                specification = specification.and(buildSpecification(criteria.getIsPublic(), Media_.isPublic));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Media_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Media_.updatedAt));
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEventId(), root -> root.join(Media_.event, JoinType.LEFT).get(Event_.id))
                    );
            }
            if (criteria.getUploadedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUploadedById(),
                            root -> root.join(Media_.uploadedBy, JoinType.LEFT).get(UserProfile_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
