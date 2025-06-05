package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.EventDetailsTestSamples.*;
import static com.nextjstemplate.domain.EventGuestPricingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventGuestPricingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventGuestPricing.class);
        EventGuestPricing eventGuestPricing1 = getEventGuestPricingSample1();
        EventGuestPricing eventGuestPricing2 = new EventGuestPricing();
        assertThat(eventGuestPricing1).isNotEqualTo(eventGuestPricing2);

        eventGuestPricing2.setId(eventGuestPricing1.getId());
        assertThat(eventGuestPricing1).isEqualTo(eventGuestPricing2);

        eventGuestPricing2 = getEventGuestPricingSample2();
        assertThat(eventGuestPricing1).isNotEqualTo(eventGuestPricing2);
    }

    @Test
    void eventTest() throws Exception {
        EventGuestPricing eventGuestPricing = getEventGuestPricingRandomSampleGenerator();
        EventDetails eventDetailsBack = getEventDetailsRandomSampleGenerator();

        eventGuestPricing.setEvent(eventDetailsBack);
        assertThat(eventGuestPricing.getEvent()).isEqualTo(eventDetailsBack);

        eventGuestPricing.event(null);
        assertThat(eventGuestPricing.getEvent()).isNull();
    }
}
