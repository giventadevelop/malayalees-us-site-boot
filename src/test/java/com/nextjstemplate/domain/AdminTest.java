package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.AdminTestSamples.*;
import static com.nextjstemplate.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdminTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Admin.class);
        Admin admin1 = getAdminSample1();
        Admin admin2 = new Admin();
        assertThat(admin1).isNotEqualTo(admin2);

        admin2.setId(admin1.getId());
        assertThat(admin1).isEqualTo(admin2);

        admin2 = getAdminSample2();
        assertThat(admin1).isNotEqualTo(admin2);
    }

    @Test
    void userTest() throws Exception {
        Admin admin = getAdminRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        admin.setUser(userProfileBack);
        assertThat(admin.getUser()).isEqualTo(userProfileBack);

        admin.user(null);
        assertThat(admin.getUser()).isNull();
    }

    @Test
    void createdByTest() throws Exception {
        Admin admin = getAdminRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        admin.setCreatedBy(userProfileBack);
        assertThat(admin.getCreatedBy()).isEqualTo(userProfileBack);

        admin.createdBy(null);
        assertThat(admin.getCreatedBy()).isNull();
    }
}
