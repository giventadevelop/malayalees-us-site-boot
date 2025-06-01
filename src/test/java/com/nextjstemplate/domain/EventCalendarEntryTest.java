package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.EventCalendarEntryTestSamples.*;
import static com.nextjstemplate.domain.EventDetailsTestSamples.*;
import static com.nextjstemplate.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventCalendarEntryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventCalendarEntry.class);
        EventCalendarEntry eventCalendarEntry1 = getEventCalendarEntrySample1();
        EventCalendarEntry eventCalendarEntry2 = new EventCalendarEntry();
        assertThat(eventCalendarEntry1).isNotEqualTo(eventCalendarEntry2);

        eventCalendarEntry2.setId(eventCalendarEntry1.getId());
        assertThat(eventCalendarEntry1).isEqualTo(eventCalendarEntry2);

        eventCalendarEntry2 = getEventCalendarEntrySample2();
        assertThat(eventCalendarEntry1).isNotEqualTo(eventCalendarEntry2);
    }

    @Test
    void eventTest() throws Exception {
        EventCalendarEntry eventCalendarEntry = getEventCalendarEntryRandomSampleGenerator();
        EventDetails eventDetailsBack = getEventDetailsRandomSampleGenerator();

        eventCalendarEntry.setEvent(eventDetailsBack);
        assertThat(eventCalendarEntry.getEvent()).isEqualTo(eventDetailsBack);

        eventCalendarEntry.event(null);
        assertThat(eventCalendarEntry.getEvent()).isNull();
    }

    @Test
    void createdByTest() throws Exception {
        EventCalendarEntry eventCalendarEntry = getEventCalendarEntryRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        eventCalendarEntry.setCreatedBy(userProfileBack);
        assertThat(eventCalendarEntry.getCreatedBy()).isEqualTo(userProfileBack);

        eventCalendarEntry.createdBy(null);
        assertThat(eventCalendarEntry.getCreatedBy()).isNull();
    }
}
