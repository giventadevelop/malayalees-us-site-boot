package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventTypeDetails;
import com.nextjstemplate.service.dto.EventTypeDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventTypeDetails} and its DTO {@link EventTypeDetailsDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventTypeDetailsMapper extends EntityMapper<EventTypeDetailsDTO, EventTypeDetails> {}
