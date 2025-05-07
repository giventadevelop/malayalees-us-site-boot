package com.nextjstemplate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PollResponseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PollResponse getPollResponseSample1() {
        return new PollResponse().id(1L).comment("comment1");
    }

    public static PollResponse getPollResponseSample2() {
        return new PollResponse().id(2L).comment("comment2");
    }

    public static PollResponse getPollResponseRandomSampleGenerator() {
        return new PollResponse().id(longCount.incrementAndGet()).comment(UUID.randomUUID().toString());
    }
}
