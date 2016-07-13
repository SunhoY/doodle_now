package io.harry.doodlenow.model;

import org.joda.time.DateTime;

public class Doodle {
    private String title;
    private String content;

    private String elapsedHours;

    public Doodle(DoodleJson doodleJson) {
        this.title = doodleJson.title;
        this.content = doodleJson.content;

        int elapsedHours = new DateTime().getHourOfDay() - new DateTime(doodleJson.createdAt).getHourOfDay();
        this.elapsedHours = elapsedHours + "hours ago";
    }

    public Doodle(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Doodle doodle = (Doodle) o;

        if (title != null ? !title.equals(doodle.title) : doodle.title != null) return false;
        if (content != null ? !content.equals(doodle.content) : doodle.content != null)
            return false;
        return elapsedHours != null ? elapsedHours.equals(doodle.elapsedHours) : doodle.elapsedHours == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (elapsedHours != null ? elapsedHours.hashCode() : 0);
        return result;
    }
}
