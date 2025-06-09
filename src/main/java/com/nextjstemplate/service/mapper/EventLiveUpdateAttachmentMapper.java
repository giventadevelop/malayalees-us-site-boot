package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventLiveUpdate;
import com.nextjstemplate.domain.EventLiveUpdateAttachment;
import com.nextjstemplate.service.dto.EventLiveUpdateAttachmentDTO;
import com.nextjstemplate.service.dto.EventLiveUpdateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventLiveUpdateAttachment} and its DTO {@link EventLiveUpdateAttachmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventLiveUpdateAttachmentMapper extends EntityMapper<EventLiveUpdateAttachmentDTO, EventLiveUpdateAttachment> {
    @Mapping(target = "liveUpdate", source = "liveUpdate", qualifiedByName = "eventLiveUpdateId")
    EventLiveUpdateAttachmentDTO toDto(EventLiveUpdateAttachment s);

    @Named("eventLiveUpdateId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventLiveUpdateDTO toDtoEventLiveUpdateId(EventLiveUpdate eventLiveUpdate);
}
