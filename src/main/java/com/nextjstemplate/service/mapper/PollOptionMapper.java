package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.Poll;
import com.nextjstemplate.domain.PollOption;
import com.nextjstemplate.service.dto.PollDTO;
import com.nextjstemplate.service.dto.PollOptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PollOption} and its DTO {@link PollOptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PollOptionMapper extends EntityMapper<PollOptionDTO, PollOption> {
    @Mapping(target = "poll", source = "poll", qualifiedByName = "pollId")
    PollOptionDTO toDto(PollOption s);

    @Named("pollId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PollDTO toDtoPollId(Poll poll);
}
