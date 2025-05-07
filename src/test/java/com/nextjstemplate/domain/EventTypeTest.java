package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.EventTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventType.class);
        EventType eventType1 = getEventTypeSample1();
        EventType eventType2 = new EventType();
        assertThat(eventType1).isNotEqualTo(eventType2);

        eventType2.setId(eventType1.getId());
        assertThat(eventType1).isEqualTo(eventType2);

        eventType2 = getEventTypeSample2();
        assertThat(eventType1).isNotEqualTo(eventType2);
    }
}
