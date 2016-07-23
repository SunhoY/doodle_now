package io.harry.doodlenow.fragment.doodlerange;

import org.joda.time.DateTime;
import org.junit.Test;

import io.harry.doodlenow.fragment.DoodleListType;

import static org.assertj.core.api.Assertions.assertThat;

public class DoodleRangeCalculatorTest {
    private static final long MILLIS_2016_06_19_10_00_00 = 1466298000000L;
    private static final long MILLIS_2016_06_19_09_59_59 = 1466297999000L;
    private static final long MILLIS_2016_06_18_10_00_00 = 1466211600000L;
    private static final long MILLIS_2016_06_12_10_00_00 = 1465693200000L;
    private static final long MILLIS_2016_06_11_10_00_00 = 1465606800000L;
    private static final int STAND_UP_STARTS_AT_10AM = 10;
    private static final int ARCHIVES_FOR_7_DAYS = 7;
    private DoodleRangeCalculator subject;

    @Test
    public void calculateRange_returnsYesterdayStandUpTimeToNow_whenTypeIsTodayAndBeforeStandUp() throws Exception {
        subject = new DoodleRangeCalculator();

        DoodleRange result = subject.calculateRange(
                DoodleListType.Today,
                new DateTime(MILLIS_2016_06_19_09_59_59),
                STAND_UP_STARTS_AT_10AM,
                ARCHIVES_FOR_7_DAYS);

        assertThat(result.getStartAt()).isEqualTo(MILLIS_2016_06_18_10_00_00);
        assertThat(result.getEndAt()).isEqualTo(MILLIS_2016_06_19_09_59_59);
    }

    @Test
    public void calculateRange_returnsTodayStandUpTimeToNow_whenTypeIsTodayAndAfterStandUp() throws Exception {
        subject = new DoodleRangeCalculator();

        DoodleRange result = subject.calculateRange(
                DoodleListType.Today,
                new DateTime(MILLIS_2016_06_19_10_00_00),
                STAND_UP_STARTS_AT_10AM,
                ARCHIVES_FOR_7_DAYS);

        assertThat(result.getStartAt()).isEqualTo(MILLIS_2016_06_19_10_00_00);
        assertThat(result.getEndAt()).isEqualTo(MILLIS_2016_06_19_10_00_00);
    }

    @Test
    public void calculateRange_returnsArchiveDurationDaysBeforeYesterday_whenTypeIsArchiveAndBeforeStandUp() throws Exception {
        subject = new DoodleRangeCalculator();

        DoodleRange result = subject.calculateRange(
                DoodleListType.Archive,
                new DateTime(MILLIS_2016_06_19_09_59_59),
                STAND_UP_STARTS_AT_10AM,
                ARCHIVES_FOR_7_DAYS);

        assertThat(result.getStartAt()).isEqualTo(MILLIS_2016_06_11_10_00_00);
        assertThat(result.getEndAt()).isEqualTo(MILLIS_2016_06_18_10_00_00);
    }

    @Test
    public void calculateRange_returnsArchiveDurationBeforeToday_whenTypeIsArchiveAndAfterStandUp() throws Exception {
        subject = new DoodleRangeCalculator();

        DoodleRange result = subject.calculateRange(
                DoodleListType.Archive,
                new DateTime(MILLIS_2016_06_19_10_00_00),
                STAND_UP_STARTS_AT_10AM,
                ARCHIVES_FOR_7_DAYS);

        assertThat(result.getStartAt()).isEqualTo(MILLIS_2016_06_12_10_00_00);
        assertThat(result.getEndAt()).isEqualTo(MILLIS_2016_06_19_10_00_00);
    }
}