package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.BulkOperationLog;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.service.dto.BulkOperationLogDTO;
import com.nextjstemplate.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BulkOperationLog} and its DTO {@link BulkOperationLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface BulkOperationLogMapper extends EntityMapper<BulkOperationLogDTO, BulkOperationLog> {
    @Mapping(target = "performedBy", source = "performedBy", qualifiedByName = "userProfileId")
    BulkOperationLogDTO toDto(BulkOperationLog s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
