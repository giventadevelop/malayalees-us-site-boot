package com.nextjstemplate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TicketTransactionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TicketTransaction getTicketTransactionSample1() {
        return new TicketTransaction().id(1L).email("email1").firstName("firstName1").lastName("lastName1").quantity(1).status("status1");
    }

    public static TicketTransaction getTicketTransactionSample2() {
        return new TicketTransaction().id(2L).email("email2").firstName("firstName2").lastName("lastName2").quantity(2).status("status2");
    }

    public static TicketTransaction getTicketTransactionRandomSampleGenerator() {
        return new TicketTransaction()
            .id(longCount.incrementAndGet())
            .email(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .quantity(intCount.incrementAndGet())
            .status(UUID.randomUUID().toString());
    }
}
