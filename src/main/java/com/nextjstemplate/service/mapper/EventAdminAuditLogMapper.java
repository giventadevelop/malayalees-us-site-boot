package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventAdminAuditLog;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.service.dto.EventAdminAuditLogDTO;
import com.nextjstemplate.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventAdminAuditLog} and its DTO {@link EventAdminAuditLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventAdminAuditLogMapper extends EntityMapper<EventAdminAuditLogDTO, EventAdminAuditLog> {
    @Mapping(target = "admin", source = "admin", qualifiedByName = "userProfileId")
    EventAdminAuditLogDTO toDto(EventAdminAuditLog s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
