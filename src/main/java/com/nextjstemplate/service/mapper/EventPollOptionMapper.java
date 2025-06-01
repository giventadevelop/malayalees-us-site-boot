package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventPoll;
import com.nextjstemplate.domain.EventPollOption;
import com.nextjstemplate.service.dto.EventPollDTO;
import com.nextjstemplate.service.dto.EventPollOptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventPollOption} and its DTO {@link EventPollOptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventPollOptionMapper extends EntityMapper<EventPollOptionDTO, EventPollOption> {
    @Mapping(target = "poll", source = "poll", qualifiedByName = "eventPollId")
    EventPollOptionDTO toDto(EventPollOption s);

    @Named("eventPollId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventPollDTO toDtoEventPollId(EventPoll eventPoll);
}
