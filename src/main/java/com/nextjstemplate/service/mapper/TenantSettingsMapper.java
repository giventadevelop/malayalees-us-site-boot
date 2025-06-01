package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.TenantSettings;
import com.nextjstemplate.service.dto.TenantSettingsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TenantSettings} and its DTO
 * {@link TenantSettingsDTO}.
 */
@Mapper(componentModel = "spring")
public interface TenantSettingsMapper extends EntityMapper<TenantSettingsDTO, TenantSettings> {
    TenantSettingsDTO toDto(TenantSettings s);
}
