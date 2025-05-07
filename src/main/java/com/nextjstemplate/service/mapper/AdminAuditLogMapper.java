package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.AdminAuditLog;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.service.dto.AdminAuditLogDTO;
import com.nextjstemplate.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AdminAuditLog} and its DTO {@link AdminAuditLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdminAuditLogMapper extends EntityMapper<AdminAuditLogDTO, AdminAuditLog> {
    @Mapping(target = "admin", source = "admin", qualifiedByName = "userProfileId")
    AdminAuditLogDTO toDto(AdminAuditLog s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
