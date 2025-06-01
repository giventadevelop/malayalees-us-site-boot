package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.domain.UserRegistrationRequest;
import com.nextjstemplate.service.dto.UserProfileDTO;
import com.nextjstemplate.service.dto.UserRegistrationRequestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserRegistrationRequest} and its DTO {@link UserRegistrationRequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserRegistrationRequestMapper extends EntityMapper<UserRegistrationRequestDTO, UserRegistrationRequest> {
    @Mapping(target = "reviewedBy", source = "reviewedBy", qualifiedByName = "userProfileId")
    UserRegistrationRequestDTO toDto(UserRegistrationRequest s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
