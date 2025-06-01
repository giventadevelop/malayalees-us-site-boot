package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.EventAdminAuditLogTestSamples.*;
import static com.nextjstemplate.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventAdminAuditLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventAdminAuditLog.class);
        EventAdminAuditLog eventAdminAuditLog1 = getEventAdminAuditLogSample1();
        EventAdminAuditLog eventAdminAuditLog2 = new EventAdminAuditLog();
        assertThat(eventAdminAuditLog1).isNotEqualTo(eventAdminAuditLog2);

        eventAdminAuditLog2.setId(eventAdminAuditLog1.getId());
        assertThat(eventAdminAuditLog1).isEqualTo(eventAdminAuditLog2);

        eventAdminAuditLog2 = getEventAdminAuditLogSample2();
        assertThat(eventAdminAuditLog1).isNotEqualTo(eventAdminAuditLog2);
    }

    @Test
    void adminTest() throws Exception {
        EventAdminAuditLog eventAdminAuditLog = getEventAdminAuditLogRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        eventAdminAuditLog.setAdmin(userProfileBack);
        assertThat(eventAdminAuditLog.getAdmin()).isEqualTo(userProfileBack);

        eventAdminAuditLog.admin(null);
        assertThat(eventAdminAuditLog.getAdmin()).isNull();
    }
}
