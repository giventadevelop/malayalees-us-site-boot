package com.nextjstemplate.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class AdminAuditLogMapperTest {

    private AdminAuditLogMapper adminAuditLogMapper;

    @BeforeEach
    public void setUp() {
        adminAuditLogMapper = new AdminAuditLogMapperImpl();
    }
}
