package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.Poll;
import com.nextjstemplate.repository.PollRepository;
import com.nextjstemplate.service.criteria.PollCriteria;
import com.nextjstemplate.service.dto.PollDTO;
import com.nextjstemplate.service.mapper.PollMapper;
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
 * Service for executing complex queries for {@link Poll} entities in the database.
 * The main input is a {@link PollCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PollDTO} or a {@link Page} of {@link PollDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PollQueryService extends QueryService<Poll> {

    private final Logger log = LoggerFactory.getLogger(PollQueryService.class);

    private final PollRepository pollRepository;

    private final PollMapper pollMapper;

    public PollQueryService(PollRepository pollRepository, PollMapper pollMapper) {
        this.pollRepository = pollRepository;
        this.pollMapper = pollMapper;
    }

    /**
     * Return a {@link List} of {@link PollDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PollDTO> findByCriteria(PollCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Poll> specification = createSpecification(criteria);
        return pollMapper.toDto(pollRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PollDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PollDTO> findByCriteria(PollCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Poll> specification = createSpecification(criteria);
        return pollRepository.findAll(specification, page).map(pollMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PollCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Poll> specification = createSpecification(criteria);
        return pollRepository.count(specification);
    }

    /**
     * Function to convert {@link PollCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Poll> createSpecification(PollCriteria criteria) {
        Specification<Poll> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Poll_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Poll_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Poll_.description));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), Poll_.isActive));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Poll_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), Poll_.endDate));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Poll_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Poll_.updatedAt));
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEventId(), root -> root.join(Poll_.event, JoinType.LEFT).get(Event_.id))
                    );
            }
            if (criteria.getCreatedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreatedById(),
                            root -> root.join(Poll_.createdBy, JoinType.LEFT).get(UserProfile_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
