package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.Event;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.domain.UserTask;
import com.nextjstemplate.service.dto.EventDTO;
import com.nextjstemplate.service.dto.UserProfileDTO;
import com.nextjstemplate.service.dto.UserTaskDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserTask} and its DTO {@link UserTaskDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserTaskMapper extends EntityMapper<UserTaskDTO, UserTask> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userProfileId")
    @Mapping(target = "event", source = "event", qualifiedByName = "eventId")
    UserTaskDTO toDto(UserTask s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("eventId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDTO toDtoEventId(Event event);
}
