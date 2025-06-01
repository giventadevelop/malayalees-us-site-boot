package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.TenantOrganization;
import com.nextjstemplate.service.dto.TenantOrganizationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TenantOrganization} and its DTO {@link TenantOrganizationDTO}.
 */
@Mapper(componentModel = "spring")
public interface TenantOrganizationMapper extends EntityMapper<TenantOrganizationDTO, TenantOrganization> {}
