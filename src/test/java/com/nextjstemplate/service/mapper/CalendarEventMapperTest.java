package com.nextjstemplate.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class CalendarEventMapperTest {

    private CalendarEventMapper calendarEventMapper;

    @BeforeEach
    public void setUp() {
        calendarEventMapper = new CalendarEventMapperImpl();
    }
}
