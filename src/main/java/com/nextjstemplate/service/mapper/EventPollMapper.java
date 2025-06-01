package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.EventPoll;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.service.dto.EventDetailsDTO;
import com.nextjstemplate.service.dto.EventPollDTO;
import com.nextjstemplate.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventPoll} and its DTO {@link EventPollDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventPollMapper extends EntityMapper<EventPollDTO, EventPoll> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "userProfileId")
    EventPollDTO toDto(EventPoll s);

    @Named("eventDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDetailsDTO toDtoEventDetailsId(EventDetails eventDetails);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
