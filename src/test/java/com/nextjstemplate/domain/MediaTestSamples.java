package com.nextjstemplate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MediaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Media getMediaSample1() {
        return new Media()
            .id(1L)
            .title("title1")
            .description("description1")
            .mediaType("mediaType1")
            .storageType("storageType1")
            .fileUrl("fileUrl1")
            .contentType("contentType1")
            .fileSize(1);
    }

    public static Media getMediaSample2() {
        return new Media()
            .id(2L)
            .title("title2")
            .description("description2")
            .mediaType("mediaType2")
            .storageType("storageType2")
            .fileUrl("fileUrl2")
            .contentType("contentType2")
            .fileSize(2);
    }

    public static Media getMediaRandomSampleGenerator() {
        return new Media()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .mediaType(UUID.randomUUID().toString())
            .storageType(UUID.randomUUID().toString())
            .fileUrl(UUID.randomUUID().toString())
            .contentType(UUID.randomUUID().toString())
            .fileSize(intCount.incrementAndGet());
    }
}
