package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.EventTestSamples.*;
import static com.nextjstemplate.domain.EventTypeTestSamples.*;
import static com.nextjstemplate.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Event.class);
        Event event1 = getEventSample1();
        Event event2 = new Event();
        assertThat(event1).isNotEqualTo(event2);

        event2.setId(event1.getId());
        assertThat(event1).isEqualTo(event2);

        event2 = getEventSample2();
        assertThat(event1).isNotEqualTo(event2);
    }

    @Test
    void createdByTest() throws Exception {
        Event event = getEventRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        event.setCreatedBy(userProfileBack);
        assertThat(event.getCreatedBy()).isEqualTo(userProfileBack);

        event.createdBy(null);
        assertThat(event.getCreatedBy()).isNull();
    }

    @Test
    void eventTypeTest() throws Exception {
        Event event = getEventRandomSampleGenerator();
        EventType eventTypeBack = getEventTypeRandomSampleGenerator();

        event.setEventType(eventTypeBack);
        assertThat(event.getEventType()).isEqualTo(eventTypeBack);

        event.eventType(null);
        assertThat(event.getEventType()).isNull();
    }
}
