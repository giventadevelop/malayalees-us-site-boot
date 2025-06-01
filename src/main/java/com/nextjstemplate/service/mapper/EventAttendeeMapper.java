package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventAttendee;
import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.service.dto.EventAttendeeDTO;
import com.nextjstemplate.service.dto.EventDetailsDTO;
import com.nextjstemplate.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventAttendee} and its DTO {@link EventAttendeeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventAttendeeMapper extends EntityMapper<EventAttendeeDTO, EventAttendee> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
    @Mapping(target = "attendee", source = "attendee", qualifiedByName = "userProfileId")
    EventAttendeeDTO toDto(EventAttendee s);

    @Named("eventDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDetailsDTO toDtoEventDetailsId(EventDetails eventDetails);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
