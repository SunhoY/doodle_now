package io.harry.doodlenow.fragment.doodlerange;

import org.joda.time.DateTime;

import io.harry.doodlenow.fragment.DoodleListType;

public class DoodleRangeCalculator {
    public DoodleRange calculateRange(DoodleListType type, DateTime current, int standUpStartsAt, int archiveDuration) {
        long startAt;
        long endAt;
        DateTime standUpStartTime = current.withTimeAtStartOfDay().plusHours(standUpStartsAt);
        if(type == DoodleListType.Today) {
            if (current.getHourOfDay() < standUpStartsAt) {
                startAt = standUpStartTime.minusDays(1).getMillis();
            } else {
                startAt = standUpStartTime.getMillis();
            }

            endAt = current.getMillis();
        } else {
            if (current.getHourOfDay() < standUpStartsAt) {
                startAt = standUpStartTime.minusDays(archiveDuration + 1).getMillis();
                endAt = standUpStartTime.minusDays(1).getMillis();
            } else {
                startAt = standUpStartTime.minusDays(archiveDuration).getMillis();
                endAt = standUpStartTime.getMillis();
            }
        }

        return new DoodleRange(startAt, endAt);
    }
}
