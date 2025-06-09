package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.DiscountCode;
import com.nextjstemplate.service.dto.DiscountCodeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DiscountCode} and its DTO {@link DiscountCodeDTO}.
 */
@Mapper(componentModel = "spring")
public interface DiscountCodeMapper extends EntityMapper<DiscountCodeDTO, DiscountCode> {}
