package io.harry.doodlenow.model;

import org.joda.time.DateTime;

import java.io.Serializable;

public class Doodle implements Serializable {
    private final String title;
    private final String content;
    private final String imageUrl;
    private final String elapsedHours;
    private final long createdAt;
    private final String url;

    public Doodle(DoodleJson doodleJson) {
        this(doodleJson.title, doodleJson.content, doodleJson.url, doodleJson.imageUrl, doodleJson.createdAt);
    }

    public Doodle(String title, String content, String url, String imageUrl, long createdAt) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.url = url;
        this.elapsedHours = calculateElapsedHours(createdAt) + " hours ago";
    }

    private int calculateElapsedHours(long createdAt) {
        DateTime current = new DateTime();
        DateTime target = new DateTime(createdAt);

        int days = current.getDayOfYear() - target.getDayOfYear();
        int hours = current.getHourOfDay() - target.getHourOfDay();

        return days * 24 + hours;
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

    public String getUrl() {
        return url;
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
        if (elapsedHours != null ? !elapsedHours.equals(doodle.elapsedHours) : doodle.elapsedHours != null)
            return false;
        return url != null ? url.equals(doodle.url) : doodle.url == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (elapsedHours != null ? elapsedHours.hashCode() : 0);
        result = 31 * result + (int) (createdAt ^ (createdAt >>> 32));
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
