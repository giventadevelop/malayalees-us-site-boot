package com.nextjstemplate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CalendarEventTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CalendarEvent getCalendarEventSample1() {
        return new CalendarEvent()
            .id(1L)
            .calendarProvider("calendarProvider1")
            .externalEventId("externalEventId1")
            .calendarLink("calendarLink1");
    }

    public static CalendarEvent getCalendarEventSample2() {
        return new CalendarEvent()
            .id(2L)
            .calendarProvider("calendarProvider2")
            .externalEventId("externalEventId2")
            .calendarLink("calendarLink2");
    }

    public static CalendarEvent getCalendarEventRandomSampleGenerator() {
        return new CalendarEvent()
            .id(longCount.incrementAndGet())
            .calendarProvider(UUID.randomUUID().toString())
            .externalEventId(UUID.randomUUID().toString())
            .calendarLink(UUID.randomUUID().toString());
    }
}
