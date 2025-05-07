package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.EventTestSamples.*;
import static com.nextjstemplate.domain.TicketTransactionTestSamples.*;
import static com.nextjstemplate.domain.TicketTypeTestSamples.*;
import static com.nextjstemplate.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TicketTransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TicketTransaction.class);
        TicketTransaction ticketTransaction1 = getTicketTransactionSample1();
        TicketTransaction ticketTransaction2 = new TicketTransaction();
        assertThat(ticketTransaction1).isNotEqualTo(ticketTransaction2);

        ticketTransaction2.setId(ticketTransaction1.getId());
        assertThat(ticketTransaction1).isEqualTo(ticketTransaction2);

        ticketTransaction2 = getTicketTransactionSample2();
        assertThat(ticketTransaction1).isNotEqualTo(ticketTransaction2);
    }

    @Test
    void eventTest() throws Exception {
        TicketTransaction ticketTransaction = getTicketTransactionRandomSampleGenerator();
        Event eventBack = getEventRandomSampleGenerator();

        ticketTransaction.setEvent(eventBack);
        assertThat(ticketTransaction.getEvent()).isEqualTo(eventBack);

        ticketTransaction.event(null);
        assertThat(ticketTransaction.getEvent()).isNull();
    }

    @Test
    void ticketTypeTest() throws Exception {
        TicketTransaction ticketTransaction = getTicketTransactionRandomSampleGenerator();
        TicketType ticketTypeBack = getTicketTypeRandomSampleGenerator();

        ticketTransaction.setTicketType(ticketTypeBack);
        assertThat(ticketTransaction.getTicketType()).isEqualTo(ticketTypeBack);

        ticketTransaction.ticketType(null);
        assertThat(ticketTransaction.getTicketType()).isNull();
    }

    @Test
    void userTest() throws Exception {
        TicketTransaction ticketTransaction = getTicketTransactionRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        ticketTransaction.setUser(userProfileBack);
        assertThat(ticketTransaction.getUser()).isEqualTo(userProfileBack);

        ticketTransaction.user(null);
        assertThat(ticketTransaction.getUser()).isNull();
    }
}
