package io.harry.doodlenow.model;

import org.joda.time.DateTime;

public class Doodle {
    private String title;
    private String content;
    private String imageUrl;
    private String elapsedHours;
    private long createdAt;

    public Doodle(DoodleJson doodleJson) {
        this.title = doodleJson.title;
        this.content = doodleJson.content;
        this.imageUrl = doodleJson.imageUrl;
        this.createdAt = doodleJson.createdAt;
        this.elapsedHours = calculateElapsedHours(doodleJson.createdAt) + " hours ago";
    }

    public Doodle(String title, String content, String imageUrl, long createdAt) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.elapsedHours = calculateElapsedHours(createdAt) + " hours ago";
    }

    private int calculateElapsedHours(long createdAt) {
        return new DateTime().getHourOfDay() - new DateTime(createdAt).getHourOfDay();
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {return imageUrl; }

    public long getCreatedAt() {
        return createdAt;
    }

    public String getElapsedHours() {
        return elapsedHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Doodle doodle = (Doodle) o;

        if (createdAt != doodle.createdAt) return false;
        if (title != null ? !title.equals(doodle.title) : doodle.title != null) return false;
        if (content != null ? !content.equals(doodle.content) : doodle.content != null)
            return false;
        if (imageUrl != null ? !imageUrl.equals(doodle.imageUrl) : doodle.imageUrl != null)
            return false;
        return elapsedHours != null ? elapsedHours.equals(doodle.elapsedHours) : doodle.elapsedHours == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (elapsedHours != null ? elapsedHours.hashCode() : 0);
        result = 31 * result + (int) (createdAt ^ (createdAt >>> 32));
        return result;
    }
}
