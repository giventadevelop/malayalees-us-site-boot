package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.EventTypeDetailsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventTypeDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventTypeDetails.class);
        EventTypeDetails eventTypeDetails1 = getEventTypeDetailsSample1();
        EventTypeDetails eventTypeDetails2 = new EventTypeDetails();
        assertThat(eventTypeDetails1).isNotEqualTo(eventTypeDetails2);

        eventTypeDetails2.setId(eventTypeDetails1.getId());
        assertThat(eventTypeDetails1).isEqualTo(eventTypeDetails2);

        eventTypeDetails2 = getEventTypeDetailsSample2();
        assertThat(eventTypeDetails1).isNotEqualTo(eventTypeDetails2);
    }
}
