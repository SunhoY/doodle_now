package io.harry.doodlenow.model;

public class DoodleJson {
    public final String title;
    public final String content;
    public final String imageUrl;
    public final long createdAt;

    public DoodleJson(Doodle doodle) {
        this.title = doodle.getTitle();
        this.content = doodle.getContent();
        this.imageUrl = doodle.getImageUrl();
        this.createdAt = doodle.getCreatedAt();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoodleJson that = (DoodleJson) o;

        if (createdAt != that.createdAt) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return content != null ? content.equals(that.content) : that.content == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (int) (createdAt ^ (createdAt >>> 32));
        return result;
    }
}
