package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.EventTypeDetails;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.service.dto.EventDetailsDTO;
import com.nextjstemplate.service.dto.EventTypeDetailsDTO;
import com.nextjstemplate.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventDetails} and its DTO {@link EventDetailsDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventDetailsMapper extends EntityMapper<EventDetailsDTO, EventDetails> {
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "userProfileId")
    @Mapping(target = "eventType", source = "eventType", qualifiedByName = "eventTypeDetailsId")
    EventDetailsDTO toDto(EventDetails s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("eventTypeDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventTypeDetailsDTO toDtoEventTypeDetailsId(EventTypeDetails eventTypeDetails);
}
