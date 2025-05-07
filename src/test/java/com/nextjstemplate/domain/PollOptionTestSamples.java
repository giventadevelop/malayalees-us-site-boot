package com.nextjstemplate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PollOptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PollOption getPollOptionSample1() {
        return new PollOption().id(1L).optionText("optionText1");
    }

    public static PollOption getPollOptionSample2() {
        return new PollOption().id(2L).optionText("optionText2");
    }

    public static PollOption getPollOptionRandomSampleGenerator() {
        return new PollOption().id(longCount.incrementAndGet()).optionText(UUID.randomUUID().toString());
    }
}
