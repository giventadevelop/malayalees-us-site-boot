package com.nextjstemplate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EventTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Event getEventSample1() {
        return new Event()
            .id(1L)
            .title("title1")
            .caption("caption1")
            .description("description1")
            .location("location1")
            .capacity(1)
            .admissionType("admissionType1");
    }

    public static Event getEventSample2() {
        return new Event()
            .id(2L)
            .title("title2")
            .caption("caption2")
            .description("description2")
            .location("location2")
            .capacity(2)
            .admissionType("admissionType2");
    }

    public static Event getEventRandomSampleGenerator() {
        return new Event()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .caption(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString())
            .capacity(intCount.incrementAndGet())
            .admissionType(UUID.randomUUID().toString());
    }
}
