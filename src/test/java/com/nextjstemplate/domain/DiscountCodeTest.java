package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.DiscountCodeTestSamples.*;
import static com.nextjstemplate.domain.EventDetailsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DiscountCodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DiscountCode.class);
        DiscountCode discountCode1 = getDiscountCodeSample1();
        DiscountCode discountCode2 = new DiscountCode();
        assertThat(discountCode1).isNotEqualTo(discountCode2);

        discountCode2.setId(discountCode1.getId());
        assertThat(discountCode1).isEqualTo(discountCode2);

        discountCode2 = getDiscountCodeSample2();
        assertThat(discountCode1).isNotEqualTo(discountCode2);
    }

    @Test
    void eventsTest() throws Exception {
        DiscountCode discountCode = getDiscountCodeRandomSampleGenerator();
        EventDetails eventDetailsBack = getEventDetailsRandomSampleGenerator();

        discountCode.addEvents(eventDetailsBack);
        assertThat(discountCode.getEvents()).containsOnly(eventDetailsBack);
        assertThat(eventDetailsBack.getDiscountCodes()).containsOnly(discountCode);

        discountCode.removeEvents(eventDetailsBack);
        assertThat(discountCode.getEvents()).doesNotContain(eventDetailsBack);
        assertThat(eventDetailsBack.getDiscountCodes()).doesNotContain(discountCode);

        discountCode.events(new HashSet<>(Set.of(eventDetailsBack)));
        assertThat(discountCode.getEvents()).containsOnly(eventDetailsBack);
        assertThat(eventDetailsBack.getDiscountCodes()).containsOnly(discountCode);

        discountCode.setEvents(new HashSet<>());
        assertThat(discountCode.getEvents()).doesNotContain(eventDetailsBack);
        assertThat(eventDetailsBack.getDiscountCodes()).doesNotContain(discountCode);
    }
}
