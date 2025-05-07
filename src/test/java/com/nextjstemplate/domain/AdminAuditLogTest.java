package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.AdminAuditLogTestSamples.*;
import static com.nextjstemplate.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdminAuditLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdminAuditLog.class);
        AdminAuditLog adminAuditLog1 = getAdminAuditLogSample1();
        AdminAuditLog adminAuditLog2 = new AdminAuditLog();
        assertThat(adminAuditLog1).isNotEqualTo(adminAuditLog2);

        adminAuditLog2.setId(adminAuditLog1.getId());
        assertThat(adminAuditLog1).isEqualTo(adminAuditLog2);

        adminAuditLog2 = getAdminAuditLogSample2();
        assertThat(adminAuditLog1).isNotEqualTo(adminAuditLog2);
    }

    @Test
    void adminTest() throws Exception {
        AdminAuditLog adminAuditLog = getAdminAuditLogRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        adminAuditLog.setAdmin(userProfileBack);
        assertThat(adminAuditLog.getAdmin()).isEqualTo(userProfileBack);

        adminAuditLog.admin(null);
        assertThat(adminAuditLog.getAdmin()).isNull();
    }
}
