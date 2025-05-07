package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.PollOptionTestSamples.*;
import static com.nextjstemplate.domain.PollResponseTestSamples.*;
import static com.nextjstemplate.domain.PollTestSamples.*;
import static com.nextjstemplate.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PollResponseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PollResponse.class);
        PollResponse pollResponse1 = getPollResponseSample1();
        PollResponse pollResponse2 = new PollResponse();
        assertThat(pollResponse1).isNotEqualTo(pollResponse2);

        pollResponse2.setId(pollResponse1.getId());
        assertThat(pollResponse1).isEqualTo(pollResponse2);

        pollResponse2 = getPollResponseSample2();
        assertThat(pollResponse1).isNotEqualTo(pollResponse2);
    }

    @Test
    void pollTest() throws Exception {
        PollResponse pollResponse = getPollResponseRandomSampleGenerator();
        Poll pollBack = getPollRandomSampleGenerator();

        pollResponse.setPoll(pollBack);
        assertThat(pollResponse.getPoll()).isEqualTo(pollBack);

        pollResponse.poll(null);
        assertThat(pollResponse.getPoll()).isNull();
    }

    @Test
    void pollOptionTest() throws Exception {
        PollResponse pollResponse = getPollResponseRandomSampleGenerator();
        PollOption pollOptionBack = getPollOptionRandomSampleGenerator();

        pollResponse.setPollOption(pollOptionBack);
        assertThat(pollResponse.getPollOption()).isEqualTo(pollOptionBack);

        pollResponse.pollOption(null);
        assertThat(pollResponse.getPollOption()).isNull();
    }

    @Test
    void userTest() throws Exception {
        PollResponse pollResponse = getPollResponseRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        pollResponse.setUser(userProfileBack);
        assertThat(pollResponse.getUser()).isEqualTo(userProfileBack);

        pollResponse.user(null);
        assertThat(pollResponse.getUser()).isNull();
    }
}
