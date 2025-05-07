package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.PollOptionTestSamples.*;
import static com.nextjstemplate.domain.PollTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PollOptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PollOption.class);
        PollOption pollOption1 = getPollOptionSample1();
        PollOption pollOption2 = new PollOption();
        assertThat(pollOption1).isNotEqualTo(pollOption2);

        pollOption2.setId(pollOption1.getId());
        assertThat(pollOption1).isEqualTo(pollOption2);

        pollOption2 = getPollOptionSample2();
        assertThat(pollOption1).isNotEqualTo(pollOption2);
    }

    @Test
    void pollTest() throws Exception {
        PollOption pollOption = getPollOptionRandomSampleGenerator();
        Poll pollBack = getPollRandomSampleGenerator();

        pollOption.setPoll(pollBack);
        assertThat(pollOption.getPoll()).isEqualTo(pollBack);

        pollOption.poll(null);
        assertThat(pollOption.getPoll()).isNull();
    }
}
