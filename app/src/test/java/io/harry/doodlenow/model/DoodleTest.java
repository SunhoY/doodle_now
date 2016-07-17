package io.harry.doodlenow.model;

import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DoodleTest {
    private Doodle subject;

    private long MILLIS_2016_7_19_4_0 = 1468868400000L;
    private long MILLIS_2016_7_19_2_0 = 1468861200000L;
    private long MILLIS_2016_7_18_2_0 = 1468774800000L;

    @Before
    public void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(MILLIS_2016_7_19_4_0);
    }

    @Test
    public void constructor_setsElapsedHours() throws Exception {
        subject = new Doodle("title", "content", "image url", MILLIS_2016_7_19_2_0);

        assertThat(subject.getElapsedHours()).isEqualTo("2 hours ago");
    }

    @Test
    public void constructor_setsElapsedHours_whenDaysPassed() throws Exception {
        subject = new Doodle("title", "content", "image url", MILLIS_2016_7_18_2_0);

        assertThat(subject.getElapsedHours()).isEqualTo("26 hours ago");
    }
}