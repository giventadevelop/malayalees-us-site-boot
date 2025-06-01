package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventAttendee;
import com.nextjstemplate.domain.QrCodeUsage;
import com.nextjstemplate.service.dto.EventAttendeeDTO;
import com.nextjstemplate.service.dto.QrCodeUsageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QrCodeUsage} and its DTO {@link QrCodeUsageDTO}.
 */
@Mapper(componentModel = "spring")
public interface QrCodeUsageMapper extends EntityMapper<QrCodeUsageDTO, QrCodeUsage> {
    @Mapping(target = "attendee", source = "attendee", qualifiedByName = "eventAttendeeId")
    QrCodeUsageDTO toDto(QrCodeUsage s);

    @Named("eventAttendeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventAttendeeDTO toDtoEventAttendeeId(EventAttendee eventAttendee);
}
