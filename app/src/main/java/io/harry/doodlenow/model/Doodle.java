package io.harry.doodlenow.model;

public class Doodle {
    public final String _id;
    public final String _rev;
    public final String title;
    public final String content;
    public final String url;

    public Doodle(String id, String title, String content, String url) {
        this._rev = "";
        this.title = title;
        this.content = content;
        this.url = url;
        this._id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Doodle doodle = (Doodle) o;

        if (_id != null ? !_id.equals(doodle._id) : doodle._id != null) return false;
        if (title != null ? !title.equals(doodle.title) : doodle.title != null) return false;
        if (content != null ? !content.equals(doodle.content) : doodle.content != null)
            return false;
        return url != null ? url.equals(doodle.url) : doodle.url == null;

    }

    @Override
    public int hashCode() {
        int result = _id != null ? _id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
