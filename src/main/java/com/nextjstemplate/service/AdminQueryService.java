package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.Admin;
import com.nextjstemplate.repository.AdminRepository;
import com.nextjstemplate.service.criteria.AdminCriteria;
import com.nextjstemplate.service.dto.AdminDTO;
import com.nextjstemplate.service.mapper.AdminMapper;
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
 * Service for executing complex queries for {@link Admin} entities in the database.
 * The main input is a {@link AdminCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AdminDTO} or a {@link Page} of {@link AdminDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AdminQueryService extends QueryService<Admin> {

    private final Logger log = LoggerFactory.getLogger(AdminQueryService.class);

    private final AdminRepository adminRepository;

    private final AdminMapper adminMapper;

    public AdminQueryService(AdminRepository adminRepository, AdminMapper adminMapper) {
        this.adminRepository = adminRepository;
        this.adminMapper = adminMapper;
    }

    /**
     * Return a {@link List} of {@link AdminDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AdminDTO> findByCriteria(AdminCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Admin> specification = createSpecification(criteria);
        return adminMapper.toDto(adminRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AdminDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AdminDTO> findByCriteria(AdminCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Admin> specification = createSpecification(criteria);
        return adminRepository.findAll(specification, page).map(adminMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AdminCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Admin> specification = createSpecification(criteria);
        return adminRepository.count(specification);
    }

    /**
     * Function to convert {@link AdminCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Admin> createSpecification(AdminCriteria criteria) {
        Specification<Admin> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Admin_.id));
            }
            if (criteria.getRole() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRole(), Admin_.role));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Admin_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Admin_.updatedAt));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Admin_.user, JoinType.LEFT).get(UserProfile_.id))
                    );
            }
            if (criteria.getCreatedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreatedById(),
                            root -> root.join(Admin_.createdBy, JoinType.LEFT).get(UserProfile_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
