package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.EventDetailsTestSamples.*;
import static com.nextjstemplate.domain.EventTicketTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventTicketTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventTicketType.class);
        EventTicketType eventTicketType1 = getEventTicketTypeSample1();
        EventTicketType eventTicketType2 = new EventTicketType();
        assertThat(eventTicketType1).isNotEqualTo(eventTicketType2);

        eventTicketType2.setId(eventTicketType1.getId());
        assertThat(eventTicketType1).isEqualTo(eventTicketType2);

        eventTicketType2 = getEventTicketTypeSample2();
        assertThat(eventTicketType1).isNotEqualTo(eventTicketType2);
    }

    @Test
    void eventTest() throws Exception {
        EventTicketType eventTicketType = getEventTicketTypeRandomSampleGenerator();
        EventDetails eventDetailsBack = getEventDetailsRandomSampleGenerator();

        eventTicketType.setEvent(eventDetailsBack);
        assertThat(eventTicketType.getEvent()).isEqualTo(eventDetailsBack);

        eventTicketType.event(null);
        assertThat(eventTicketType.getEvent()).isNull();
    }
}
