package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.Event;
import com.nextjstemplate.domain.Poll;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.service.dto.EventDTO;
import com.nextjstemplate.service.dto.PollDTO;
import com.nextjstemplate.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Poll} and its DTO {@link PollDTO}.
 */
@Mapper(componentModel = "spring")
public interface PollMapper extends EntityMapper<PollDTO, Poll> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventId")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "userProfileId")
    PollDTO toDto(Poll s);

    @Named("eventId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDTO toDtoEventId(Event event);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
