package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.EventTicketTransaction;
import com.nextjstemplate.domain.UserPaymentTransaction;
import com.nextjstemplate.service.dto.EventDetailsDTO;
import com.nextjstemplate.service.dto.EventTicketTransactionDTO;
import com.nextjstemplate.service.dto.UserPaymentTransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserPaymentTransaction} and its DTO {@link UserPaymentTransactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserPaymentTransactionMapper extends EntityMapper<UserPaymentTransactionDTO, UserPaymentTransaction> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
    @Mapping(target = "ticketTransaction", source = "ticketTransaction", qualifiedByName = "eventTicketTransactionId")
    UserPaymentTransactionDTO toDto(UserPaymentTransaction s);

    @Named("eventDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDetailsDTO toDtoEventDetailsId(EventDetails eventDetails);

    @Named("eventTicketTransactionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventTicketTransactionDTO toDtoEventTicketTransactionId(EventTicketTransaction eventTicketTransaction);
}
