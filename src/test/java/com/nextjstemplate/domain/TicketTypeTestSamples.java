package com.nextjstemplate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TicketTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TicketType getTicketTypeSample1() {
        return new TicketType().id(1L).name("name1").description("description1").code("code1").availableQuantity(1);
    }

    public static TicketType getTicketTypeSample2() {
        return new TicketType().id(2L).name("name2").description("description2").code("code2").availableQuantity(2);
    }

    public static TicketType getTicketTypeRandomSampleGenerator() {
        return new TicketType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .availableQuantity(intCount.incrementAndGet());
    }
}
