package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.EventOrganizer;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.service.dto.EventDetailsDTO;
import com.nextjstemplate.service.dto.EventOrganizerDTO;
import com.nextjstemplate.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventOrganizer} and its DTO {@link EventOrganizerDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventOrganizerMapper extends EntityMapper<EventOrganizerDTO, EventOrganizer> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
    @Mapping(target = "organizer", source = "organizer", qualifiedByName = "userProfileId")
    EventOrganizerDTO toDto(EventOrganizer s);

    @Named("eventDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDetailsDTO toDtoEventDetailsId(EventDetails eventDetails);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
