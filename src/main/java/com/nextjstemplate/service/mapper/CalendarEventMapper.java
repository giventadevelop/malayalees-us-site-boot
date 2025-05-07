package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.CalendarEvent;
import com.nextjstemplate.domain.Event;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.service.dto.CalendarEventDTO;
import com.nextjstemplate.service.dto.EventDTO;
import com.nextjstemplate.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CalendarEvent} and its DTO {@link CalendarEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface CalendarEventMapper extends EntityMapper<CalendarEventDTO, CalendarEvent> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventId")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "userProfileId")
    CalendarEventDTO toDto(CalendarEvent s);

    @Named("eventId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDTO toDtoEventId(Event event);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
