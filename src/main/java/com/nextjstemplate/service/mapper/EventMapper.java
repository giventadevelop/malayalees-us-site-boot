package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.Event;
import com.nextjstemplate.domain.EventType;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.service.dto.EventDTO;
import com.nextjstemplate.service.dto.EventTypeDTO;
import com.nextjstemplate.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Event} and its DTO {@link EventDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventMapper extends EntityMapper<EventDTO, Event> {
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "userProfileId")
    @Mapping(target = "eventType", source = "eventType", qualifiedByName = "eventTypeId")
    EventDTO toDto(Event s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("eventTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventTypeDTO toDtoEventTypeId(EventType eventType);
}
