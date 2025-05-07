package com.nextjstemplate.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class PollMapperTest {

    private PollMapper pollMapper;

    @BeforeEach
    public void setUp() {
        pollMapper = new PollMapperImpl();
    }
}
