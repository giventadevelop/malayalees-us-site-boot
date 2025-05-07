package com.nextjstemplate.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdminAuditLogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdminAuditLogDTO.class);
        AdminAuditLogDTO adminAuditLogDTO1 = new AdminAuditLogDTO();
        adminAuditLogDTO1.setId(1L);
        AdminAuditLogDTO adminAuditLogDTO2 = new AdminAuditLogDTO();
        assertThat(adminAuditLogDTO1).isNotEqualTo(adminAuditLogDTO2);
        adminAuditLogDTO2.setId(adminAuditLogDTO1.getId());
        assertThat(adminAuditLogDTO1).isEqualTo(adminAuditLogDTO2);
        adminAuditLogDTO2.setId(2L);
        assertThat(adminAuditLogDTO1).isNotEqualTo(adminAuditLogDTO2);
        adminAuditLogDTO1.setId(null);
        assertThat(adminAuditLogDTO1).isNotEqualTo(adminAuditLogDTO2);
    }
}
