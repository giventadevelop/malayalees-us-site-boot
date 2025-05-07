package com.nextjstemplate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EventTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EventType getEventTypeSample1() {
        return new EventType().id(1L).name("name1").description("description1");
    }

    public static EventType getEventTypeSample2() {
        return new EventType().id(2L).name("name2").description("description2");
    }

    public static EventType getEventTypeRandomSampleGenerator() {
        return new EventType().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).description(UUID.randomUUID().toString());
    }
}
