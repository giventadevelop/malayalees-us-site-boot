package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.Event;
import com.nextjstemplate.domain.TicketType;
import com.nextjstemplate.service.dto.EventDTO;
import com.nextjstemplate.service.dto.TicketTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TicketType} and its DTO {@link TicketTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface TicketTypeMapper extends EntityMapper<TicketTypeDTO, TicketType> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventId")
    TicketTypeDTO toDto(TicketType s);

    @Named("eventId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDTO toDtoEventId(Event event);
}
