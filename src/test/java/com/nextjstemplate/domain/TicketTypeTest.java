package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.EventTestSamples.*;
import static com.nextjstemplate.domain.TicketTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TicketTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TicketType.class);
        TicketType ticketType1 = getTicketTypeSample1();
        TicketType ticketType2 = new TicketType();
        assertThat(ticketType1).isNotEqualTo(ticketType2);

        ticketType2.setId(ticketType1.getId());
        assertThat(ticketType1).isEqualTo(ticketType2);

        ticketType2 = getTicketTypeSample2();
        assertThat(ticketType1).isNotEqualTo(ticketType2);
    }

    @Test
    void eventTest() throws Exception {
        TicketType ticketType = getTicketTypeRandomSampleGenerator();
        Event eventBack = getEventRandomSampleGenerator();

        ticketType.setEvent(eventBack);
        assertThat(ticketType.getEvent()).isEqualTo(eventBack);

        ticketType.event(null);
        assertThat(ticketType.getEvent()).isNull();
    }
}
