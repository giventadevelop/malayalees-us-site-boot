package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.Poll;
import com.nextjstemplate.domain.PollOption;
import com.nextjstemplate.domain.PollResponse;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.service.dto.PollDTO;
import com.nextjstemplate.service.dto.PollOptionDTO;
import com.nextjstemplate.service.dto.PollResponseDTO;
import com.nextjstemplate.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PollResponse} and its DTO {@link PollResponseDTO}.
 */
@Mapper(componentModel = "spring")
public interface PollResponseMapper extends EntityMapper<PollResponseDTO, PollResponse> {
    @Mapping(target = "poll", source = "poll", qualifiedByName = "pollId")
    @Mapping(target = "pollOption", source = "pollOption", qualifiedByName = "pollOptionId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userProfileId")
    PollResponseDTO toDto(PollResponse s);

    @Named("pollId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PollDTO toDtoPollId(Poll poll);

    @Named("pollOptionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PollOptionDTO toDtoPollOptionId(PollOption pollOption);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
