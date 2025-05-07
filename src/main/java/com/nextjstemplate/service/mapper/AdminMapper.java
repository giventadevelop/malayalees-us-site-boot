package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.Admin;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.service.dto.AdminDTO;
import com.nextjstemplate.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Admin} and its DTO {@link AdminDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdminMapper extends EntityMapper<AdminDTO, Admin> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userProfileId")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "userProfileId")
    AdminDTO toDto(Admin s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
