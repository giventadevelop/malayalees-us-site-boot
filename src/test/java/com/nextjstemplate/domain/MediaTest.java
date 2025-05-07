package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.EventTestSamples.*;
import static com.nextjstemplate.domain.MediaTestSamples.*;
import static com.nextjstemplate.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MediaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Media.class);
        Media media1 = getMediaSample1();
        Media media2 = new Media();
        assertThat(media1).isNotEqualTo(media2);

        media2.setId(media1.getId());
        assertThat(media1).isEqualTo(media2);

        media2 = getMediaSample2();
        assertThat(media1).isNotEqualTo(media2);
    }

    @Test
    void eventTest() throws Exception {
        Media media = getMediaRandomSampleGenerator();
        Event eventBack = getEventRandomSampleGenerator();

        media.setEvent(eventBack);
        assertThat(media.getEvent()).isEqualTo(eventBack);

        media.event(null);
        assertThat(media.getEvent()).isNull();
    }

    @Test
    void uploadedByTest() throws Exception {
        Media media = getMediaRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        media.setUploadedBy(userProfileBack);
        assertThat(media.getUploadedBy()).isEqualTo(userProfileBack);

        media.uploadedBy(null);
        assertThat(media.getUploadedBy()).isNull();
    }
}
