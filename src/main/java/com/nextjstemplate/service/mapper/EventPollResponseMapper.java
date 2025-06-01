package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventPoll;
import com.nextjstemplate.domain.EventPollOption;
import com.nextjstemplate.domain.EventPollResponse;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.service.dto.EventPollDTO;
import com.nextjstemplate.service.dto.EventPollOptionDTO;
import com.nextjstemplate.service.dto.EventPollResponseDTO;
import com.nextjstemplate.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventPollResponse} and its DTO {@link EventPollResponseDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventPollResponseMapper extends EntityMapper<EventPollResponseDTO, EventPollResponse> {
    @Mapping(target = "poll", source = "poll", qualifiedByName = "eventPollId")
    @Mapping(target = "pollOption", source = "pollOption", qualifiedByName = "eventPollOptionId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userProfileId")
    EventPollResponseDTO toDto(EventPollResponse s);

    @Named("eventPollId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventPollDTO toDtoEventPollId(EventPoll eventPoll);

    @Named("eventPollOptionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventPollOptionDTO toDtoEventPollOptionId(EventPollOption eventPollOption);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
