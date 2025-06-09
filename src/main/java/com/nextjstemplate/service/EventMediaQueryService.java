package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.EventMedia;
import com.nextjstemplate.repository.EventMediaRepository;
import com.nextjstemplate.service.criteria.EventMediaCriteria;
import com.nextjstemplate.service.dto.EventMediaDTO;
import com.nextjstemplate.service.mapper.EventMediaMapper;
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
 * Service for executing complex queries for {@link EventMedia} entities in the database.
 * The main input is a {@link EventMediaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventMediaDTO} or a {@link Page} of {@link EventMediaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventMediaQueryService extends QueryService<EventMedia> {

    private final Logger log = LoggerFactory.getLogger(EventMediaQueryService.class);

    private final EventMediaRepository eventMediaRepository;

    private final EventMediaMapper eventMediaMapper;

    public EventMediaQueryService(EventMediaRepository eventMediaRepository, EventMediaMapper eventMediaMapper) {
        this.eventMediaRepository = eventMediaRepository;
        this.eventMediaMapper = eventMediaMapper;
    }

    /**
     * Return a {@link List} of {@link EventMediaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventMediaDTO> findByCriteria(EventMediaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventMedia> specification = createSpecification(criteria);
        return eventMediaMapper.toDto(eventMediaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventMediaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventMediaDTO> findByCriteria(EventMediaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventMedia> specification = createSpecification(criteria);
        return eventMediaRepository.findAll(specification, page).map(eventMediaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventMediaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventMedia> specification = createSpecification(criteria);
        return eventMediaRepository.count(specification);
    }

    /**
     * Function to convert {@link EventMediaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventMedia> createSpecification(EventMediaCriteria criteria) {
        Specification<EventMedia> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventMedia_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventMedia_.tenantId));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), EventMedia_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), EventMedia_.description));
            }
            if (criteria.getEventMediaType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEventMediaType(), EventMedia_.eventMediaType));
            }
            if (criteria.getStorageType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStorageType(), EventMedia_.storageType));
            }
            if (criteria.getFileUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileUrl(), EventMedia_.fileUrl));
            }
            if (criteria.getFileDataContentType() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getFileDataContentType(), EventMedia_.fileDataContentType));
            }
            if (criteria.getFileSize() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFileSize(), EventMedia_.fileSize));
            }
            if (criteria.getIsPublic() != null) {
                specification = specification.and(buildSpecification(criteria.getIsPublic(), EventMedia_.isPublic));
            }
            if (criteria.getEventFlyer() != null) {
                specification = specification.and(buildSpecification(criteria.getEventFlyer(), EventMedia_.eventFlyer));
            }
            if (criteria.getIsEventManagementOfficialDocument() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getIsEventManagementOfficialDocument(), EventMedia_.isEventManagementOfficialDocument)
                    );
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventMedia_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventMedia_.updatedAt));
            }
            if (criteria.getPreSignedUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPreSignedUrl(), EventMedia_.preSignedUrl));
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEventId(), root -> root.join(EventMedia_.event, JoinType.LEFT).get(EventDetails_.id))
                    );
            }
            if (criteria.getUploadedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUploadedById(),
                            root -> root.join(EventMedia_.uploadedBy, JoinType.LEFT).get(UserProfile_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
