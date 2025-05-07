package com.nextjstemplate.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PollResponseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PollResponseDTO.class);
        PollResponseDTO pollResponseDTO1 = new PollResponseDTO();
        pollResponseDTO1.setId(1L);
        PollResponseDTO pollResponseDTO2 = new PollResponseDTO();
        assertThat(pollResponseDTO1).isNotEqualTo(pollResponseDTO2);
        pollResponseDTO2.setId(pollResponseDTO1.getId());
        assertThat(pollResponseDTO1).isEqualTo(pollResponseDTO2);
        pollResponseDTO2.setId(2L);
        assertThat(pollResponseDTO1).isNotEqualTo(pollResponseDTO2);
        pollResponseDTO1.setId(null);
        assertThat(pollResponseDTO1).isNotEqualTo(pollResponseDTO2);
    }
}
