package io.harry.doodlenow.fragment.doodlerange;

public class DoodleRange {
    private final long startAt;
    private final long endAt;

    public DoodleRange(long startAt, long endAt) {
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public long getStartAt() {
        return startAt;
    }

    public long getEndAt() {
        return endAt;
    }
}
