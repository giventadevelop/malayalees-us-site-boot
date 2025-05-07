package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.Event;
import com.nextjstemplate.domain.Media;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.service.dto.EventDTO;
import com.nextjstemplate.service.dto.MediaDTO;
import com.nextjstemplate.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Media} and its DTO {@link MediaDTO}.
 */
@Mapper(componentModel = "spring")
public interface MediaMapper extends EntityMapper<MediaDTO, Media> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventId")
    @Mapping(target = "uploadedBy", source = "uploadedBy", qualifiedByName = "userProfileId")
    MediaDTO toDto(Media s);

    @Named("eventId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDTO toDtoEventId(Event event);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
