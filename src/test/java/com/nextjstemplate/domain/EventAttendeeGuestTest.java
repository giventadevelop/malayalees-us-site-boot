package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.EventAttendeeGuestTestSamples.*;
import static com.nextjstemplate.domain.EventAttendeeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventAttendeeGuestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventAttendeeGuest.class);
        EventAttendeeGuest eventAttendeeGuest1 = getEventAttendeeGuestSample1();
        EventAttendeeGuest eventAttendeeGuest2 = new EventAttendeeGuest();
        assertThat(eventAttendeeGuest1).isNotEqualTo(eventAttendeeGuest2);

        eventAttendeeGuest2.setId(eventAttendeeGuest1.getId());
        assertThat(eventAttendeeGuest1).isEqualTo(eventAttendeeGuest2);

        eventAttendeeGuest2 = getEventAttendeeGuestSample2();
        assertThat(eventAttendeeGuest1).isNotEqualTo(eventAttendeeGuest2);
    }

    @Test
    void primaryAttendeeTest() throws Exception {
        EventAttendeeGuest eventAttendeeGuest = getEventAttendeeGuestRandomSampleGenerator();
        EventAttendee eventAttendeeBack = getEventAttendeeRandomSampleGenerator();

        eventAttendeeGuest.setPrimaryAttendee(eventAttendeeBack);
        assertThat(eventAttendeeGuest.getPrimaryAttendee()).isEqualTo(eventAttendeeBack);

        eventAttendeeGuest.primaryAttendee(null);
        assertThat(eventAttendeeGuest.getPrimaryAttendee()).isNull();
    }
}
