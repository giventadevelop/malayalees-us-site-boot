package com.nextjstemplate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PollTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Poll getPollSample1() {
        return new Poll().id(1L).title("title1").description("description1");
    }

    public static Poll getPollSample2() {
        return new Poll().id(2L).title("title2").description("description2");
    }

    public static Poll getPollRandomSampleGenerator() {
        return new Poll().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString()).description(UUID.randomUUID().toString());
    }
}
