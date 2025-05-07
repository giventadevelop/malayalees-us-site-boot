package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.PollResponse;
import com.nextjstemplate.repository.PollResponseRepository;
import com.nextjstemplate.service.criteria.PollResponseCriteria;
import com.nextjstemplate.service.dto.PollResponseDTO;
import com.nextjstemplate.service.mapper.PollResponseMapper;
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
 * Service for executing complex queries for {@link PollResponse} entities in the database.
 * The main input is a {@link PollResponseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PollResponseDTO} or a {@link Page} of {@link PollResponseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PollResponseQueryService extends QueryService<PollResponse> {

    private final Logger log = LoggerFactory.getLogger(PollResponseQueryService.class);

    private final PollResponseRepository pollResponseRepository;

    private final PollResponseMapper pollResponseMapper;

    public PollResponseQueryService(PollResponseRepository pollResponseRepository, PollResponseMapper pollResponseMapper) {
        this.pollResponseRepository = pollResponseRepository;
        this.pollResponseMapper = pollResponseMapper;
    }

    /**
     * Return a {@link List} of {@link PollResponseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PollResponseDTO> findByCriteria(PollResponseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PollResponse> specification = createSpecification(criteria);
        return pollResponseMapper.toDto(pollResponseRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PollResponseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PollResponseDTO> findByCriteria(PollResponseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PollResponse> specification = createSpecification(criteria);
        return pollResponseRepository.findAll(specification, page).map(pollResponseMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PollResponseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PollResponse> specification = createSpecification(criteria);
        return pollResponseRepository.count(specification);
    }

    /**
     * Function to convert {@link PollResponseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PollResponse> createSpecification(PollResponseCriteria criteria) {
        Specification<PollResponse> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PollResponse_.id));
            }
            if (criteria.getComment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComment(), PollResponse_.comment));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), PollResponse_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), PollResponse_.updatedAt));
            }
            if (criteria.getPollId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPollId(), root -> root.join(PollResponse_.poll, JoinType.LEFT).get(Poll_.id))
                    );
            }
            if (criteria.getPollOptionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPollOptionId(),
                            root -> root.join(PollResponse_.pollOption, JoinType.LEFT).get(PollOption_.id)
                        )
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(PollResponse_.user, JoinType.LEFT).get(UserProfile_.id))
                    );
            }
        }
        return specification;
    }
}
