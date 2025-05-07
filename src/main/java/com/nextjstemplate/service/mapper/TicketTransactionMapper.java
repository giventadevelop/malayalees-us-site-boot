package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.Event;
import com.nextjstemplate.domain.TicketTransaction;
import com.nextjstemplate.domain.TicketType;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.service.dto.EventDTO;
import com.nextjstemplate.service.dto.TicketTransactionDTO;
import com.nextjstemplate.service.dto.TicketTypeDTO;
import com.nextjstemplate.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TicketTransaction} and its DTO {@link TicketTransactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface TicketTransactionMapper extends EntityMapper<TicketTransactionDTO, TicketTransaction> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventId")
    @Mapping(target = "ticketType", source = "ticketType", qualifiedByName = "ticketTypeId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userProfileId")
    TicketTransactionDTO toDto(TicketTransaction s);

    @Named("eventId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDTO toDtoEventId(Event event);

    @Named("ticketTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TicketTypeDTO toDtoTicketTypeId(TicketType ticketType);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
