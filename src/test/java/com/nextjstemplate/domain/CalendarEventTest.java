package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.CalendarEventTestSamples.*;
import static com.nextjstemplate.domain.EventTestSamples.*;
import static com.nextjstemplate.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CalendarEventTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CalendarEvent.class);
        CalendarEvent calendarEvent1 = getCalendarEventSample1();
        CalendarEvent calendarEvent2 = new CalendarEvent();
        assertThat(calendarEvent1).isNotEqualTo(calendarEvent2);

        calendarEvent2.setId(calendarEvent1.getId());
        assertThat(calendarEvent1).isEqualTo(calendarEvent2);

        calendarEvent2 = getCalendarEventSample2();
        assertThat(calendarEvent1).isNotEqualTo(calendarEvent2);
    }

    @Test
    void eventTest() throws Exception {
        CalendarEvent calendarEvent = getCalendarEventRandomSampleGenerator();
        Event eventBack = getEventRandomSampleGenerator();

        calendarEvent.setEvent(eventBack);
        assertThat(calendarEvent.getEvent()).isEqualTo(eventBack);

        calendarEvent.event(null);
        assertThat(calendarEvent.getEvent()).isNull();
    }

    @Test
    void createdByTest() throws Exception {
        CalendarEvent calendarEvent = getCalendarEventRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        calendarEvent.setCreatedBy(userProfileBack);
        assertThat(calendarEvent.getCreatedBy()).isEqualTo(userProfileBack);

        calendarEvent.createdBy(null);
        assertThat(calendarEvent.getCreatedBy()).isNull();
    }
}
