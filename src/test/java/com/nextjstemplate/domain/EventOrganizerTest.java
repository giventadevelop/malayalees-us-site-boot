package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.EventOrganizerTestSamples.*;
import static com.nextjstemplate.domain.EventTestSamples.*;
import static com.nextjstemplate.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventOrganizerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventOrganizer.class);
        EventOrganizer eventOrganizer1 = getEventOrganizerSample1();
        EventOrganizer eventOrganizer2 = new EventOrganizer();
        assertThat(eventOrganizer1).isNotEqualTo(eventOrganizer2);

        eventOrganizer2.setId(eventOrganizer1.getId());
        assertThat(eventOrganizer1).isEqualTo(eventOrganizer2);

        eventOrganizer2 = getEventOrganizerSample2();
        assertThat(eventOrganizer1).isNotEqualTo(eventOrganizer2);
    }

    @Test
    void eventTest() throws Exception {
        EventOrganizer eventOrganizer = getEventOrganizerRandomSampleGenerator();
        Event eventBack = getEventRandomSampleGenerator();

        eventOrganizer.setEvent(eventBack);
        assertThat(eventOrganizer.getEvent()).isEqualTo(eventBack);

        eventOrganizer.event(null);
        assertThat(eventOrganizer.getEvent()).isNull();
    }

    @Test
    void organizerTest() throws Exception {
        EventOrganizer eventOrganizer = getEventOrganizerRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        eventOrganizer.setOrganizer(userProfileBack);
        assertThat(eventOrganizer.getOrganizer()).isEqualTo(userProfileBack);

        eventOrganizer.organizer(null);
        assertThat(eventOrganizer.getOrganizer()).isNull();
    }
}
