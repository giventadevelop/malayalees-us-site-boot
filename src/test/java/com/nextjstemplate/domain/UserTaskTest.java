package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.EventTestSamples.*;
import static com.nextjstemplate.domain.UserProfileTestSamples.*;
import static com.nextjstemplate.domain.UserTaskTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserTaskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserTask.class);
        UserTask userTask1 = getUserTaskSample1();
        UserTask userTask2 = new UserTask();
        assertThat(userTask1).isNotEqualTo(userTask2);

        userTask2.setId(userTask1.getId());
        assertThat(userTask1).isEqualTo(userTask2);

        userTask2 = getUserTaskSample2();
        assertThat(userTask1).isNotEqualTo(userTask2);
    }

    @Test
    void userTest() throws Exception {
        UserTask userTask = getUserTaskRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userTask.setUser(userProfileBack);
        assertThat(userTask.getUser()).isEqualTo(userProfileBack);

        userTask.user(null);
        assertThat(userTask.getUser()).isNull();
    }

    @Test
    void eventTest() throws Exception {
        UserTask userTask = getUserTaskRandomSampleGenerator();
        Event eventBack = getEventRandomSampleGenerator();

        userTask.setEvent(eventBack);
        assertThat(userTask.getEvent()).isEqualTo(eventBack);

        userTask.event(null);
        assertThat(userTask.getEvent()).isNull();
    }
}
