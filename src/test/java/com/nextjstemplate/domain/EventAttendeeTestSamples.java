package com.nextjstemplate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EventAttendeeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EventAttendee getEventAttendeeSample1() {
        return new EventAttendee()
            .id(1L)
            .tenantId("tenantId1")
            .registrationStatus("registrationStatus1")
            .cancellationReason("cancellationReason1")
            .attendeeType("attendeeType1")
            .specialRequirements("specialRequirements1")
            .emergencyContactName("emergencyContactName1")
            .emergencyContactPhone("emergencyContactPhone1")
            .checkInStatus("checkInStatus1")
            .notes("notes1");
    }

    public static EventAttendee getEventAttendeeSample2() {
        return new EventAttendee()
            .id(2L)
            .tenantId("tenantId2")
            .registrationStatus("registrationStatus2")
            .cancellationReason("cancellationReason2")
            .attendeeType("attendeeType2")
            .specialRequirements("specialRequirements2")
            .emergencyContactName("emergencyContactName2")
            .emergencyContactPhone("emergencyContactPhone2")
            .checkInStatus("checkInStatus2")
            .notes("notes2");
    }

    public static EventAttendee getEventAttendeeRandomSampleGenerator() {
        return new EventAttendee()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .registrationStatus(UUID.randomUUID().toString())
            .cancellationReason(UUID.randomUUID().toString())
            .attendeeType(UUID.randomUUID().toString())
            .specialRequirements(UUID.randomUUID().toString())
            .emergencyContactName(UUID.randomUUID().toString())
            .emergencyContactPhone(UUID.randomUUID().toString())
            .checkInStatus(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString());
    }
}
