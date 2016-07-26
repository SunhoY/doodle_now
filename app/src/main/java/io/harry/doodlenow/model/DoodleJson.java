package io.harry.doodlenow.model;

public class DoodleJson {
    public String title;
    public String content;
    public String imageUrl;
    public long createdAt;
    public String url;

    public DoodleJson() {}

    public DoodleJson(Doodle doodle) {
        this.title = doodle.getTitle();
        this.content = doodle.getContent();
        this.imageUrl = doodle.getImageUrl();
        this.createdAt = doodle.getCreatedAt();
        this.url = doodle.getUrl();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoodleJson that = (DoodleJson) o;

        if (createdAt != that.createdAt) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null)
            return false;
        return url != null ? url.equals(that.url) : that.url == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (int) (createdAt ^ (createdAt >>> 32));
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
