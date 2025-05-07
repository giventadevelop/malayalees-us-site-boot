package com.nextjstemplate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AdminAuditLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AdminAuditLog getAdminAuditLogSample1() {
        return new AdminAuditLog().id(1L).action("action1").tableName("tableName1").recordId("recordId1");
    }

    public static AdminAuditLog getAdminAuditLogSample2() {
        return new AdminAuditLog().id(2L).action("action2").tableName("tableName2").recordId("recordId2");
    }

    public static AdminAuditLog getAdminAuditLogRandomSampleGenerator() {
        return new AdminAuditLog()
            .id(longCount.incrementAndGet())
            .action(UUID.randomUUID().toString())
            .tableName(UUID.randomUUID().toString())
            .recordId(UUID.randomUUID().toString());
    }
}
