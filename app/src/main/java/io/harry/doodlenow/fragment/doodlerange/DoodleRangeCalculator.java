package io.harry.doodlenow.fragment.doodlerange;

import org.joda.time.DateTime;

import io.harry.doodlenow.fragment.DoodleListType;

public class DoodleRangeCalculator {
    public DoodleRange calculateRange(DoodleListType type, DateTime current, int standUpStartsAt, int archiveDuration) {
        long startAt;
        long endAt;

        if(type == DoodleListType.Today) {
            startAt = calculateStartTimeForToday(current, standUpStartsAt);
            endAt = current.getMillis();
        } else {
            startAt = calculateStartTimeForArchive(current, standUpStartsAt, archiveDuration);
            endAt = calculateEndTimeForArchive(current, standUpStartsAt);
        }

        return new DoodleRange(startAt, endAt);
    }

    private long calculateStartTimeForToday(DateTime current, int standUpStartsAt) {
        long startTimeMillis;
        boolean isWeekendOrFirstWorkingDay = getWeekendOrFirstWorkingDay(current, standUpStartsAt);

        DateTime standUpStartTime = current.withTimeAtStartOfDay().plusHours(standUpStartsAt);

        if(isWeekendOrFirstWorkingDay) {
            DateTime lastWorkingDay = getLastFriday(current);

            startTimeMillis = standUpStartTime.withDayOfMonth(lastWorkingDay.getDayOfMonth()).getMillis();
        } else if (current.getHourOfDay() < standUpStartsAt) {
            startTimeMillis = standUpStartTime.minusDays(1).getMillis();
        } else {
            startTimeMillis = standUpStartTime.getMillis();
        }
        return startTimeMillis;
    }

    private boolean getWeekendOrFirstWorkingDay(DateTime dateTime, int standUpStartsAt) {
        int dayOfWeek = dateTime.getDayOfWeek();
        if(dayOfWeek == 6 || dayOfWeek == 7) {
            return true;
        } else if(dayOfWeek == 1) {
            return dateTime.getHourOfDay() < standUpStartsAt;
        }

        return false;
    }

    private DateTime getLastFriday(DateTime dateTime) {
        DateTime result = new DateTime(dateTime.getMillis());

        while(result.getDayOfWeek() != 5) {
            result = result.minusDays(1);
        }
        return result;
    }

    private long calculateStartTimeForArchive(DateTime current, int standUpStartsAt, int archiveDuration) {
        long startAt;
        DateTime standUpStartTime = current.withTimeAtStartOfDay().plusHours(standUpStartsAt);

        if (current.getHourOfDay() < standUpStartsAt) {
            startAt = standUpStartTime.minusDays(archiveDuration + 1).getMillis();
        } else {
            startAt = standUpStartTime.minusDays(archiveDuration).getMillis();
        }

        return startAt;
    }

    private  long calculateEndTimeForArchive(DateTime current, int standUpStartsAt) {
        DateTime standUpStartTime = current.withTimeAtStartOfDay().plusHours(standUpStartsAt);
        long endAt;
        if (current.getHourOfDay() < standUpStartsAt) {
            endAt = standUpStartTime.minusDays(1).getMillis();
        } else {
            endAt = standUpStartTime.getMillis();
        }

        return endAt;
    }
}
