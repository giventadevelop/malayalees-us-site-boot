package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.EventTestSamples.*;
import static com.nextjstemplate.domain.PollTestSamples.*;
import static com.nextjstemplate.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PollTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Poll.class);
        Poll poll1 = getPollSample1();
        Poll poll2 = new Poll();
        assertThat(poll1).isNotEqualTo(poll2);

        poll2.setId(poll1.getId());
        assertThat(poll1).isEqualTo(poll2);

        poll2 = getPollSample2();
        assertThat(poll1).isNotEqualTo(poll2);
    }

    @Test
    void eventTest() throws Exception {
        Poll poll = getPollRandomSampleGenerator();
        Event eventBack = getEventRandomSampleGenerator();

        poll.setEvent(eventBack);
        assertThat(poll.getEvent()).isEqualTo(eventBack);

        poll.event(null);
        assertThat(poll.getEvent()).isNull();
    }

    @Test
    void createdByTest() throws Exception {
        Poll poll = getPollRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        poll.setCreatedBy(userProfileBack);
        assertThat(poll.getCreatedBy()).isEqualTo(userProfileBack);

        poll.createdBy(null);
        assertThat(poll.getCreatedBy()).isNull();
    }
}
