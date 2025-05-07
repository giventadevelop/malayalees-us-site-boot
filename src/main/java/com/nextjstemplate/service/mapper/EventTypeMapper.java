package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventType;
import com.nextjstemplate.service.dto.EventTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventType} and its DTO {@link EventTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventTypeMapper extends EntityMapper<EventTypeDTO, EventType> {}
