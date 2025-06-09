package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.EventLiveUpdate;
import com.nextjstemplate.service.dto.EventDetailsDTO;
import com.nextjstemplate.service.dto.EventLiveUpdateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventLiveUpdate} and its DTO {@link EventLiveUpdateDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventLiveUpdateMapper extends EntityMapper<EventLiveUpdateDTO, EventLiveUpdate> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
    EventLiveUpdateDTO toDto(EventLiveUpdate s);

    @Named("eventDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDetailsDTO toDtoEventDetailsId(EventDetails eventDetails);
}
