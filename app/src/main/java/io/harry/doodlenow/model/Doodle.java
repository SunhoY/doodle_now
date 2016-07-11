package io.harry.doodlenow.model;

public class Doodle {
    public final String _id;
    public final String _rev;
    public final String title;
    public final String content;
    public final String url;
    public long createdAt;


    public Doodle(String id, String revision, String title, String content, String url, long createdAt) {
        this._id = id;
        this._rev = revision;
        this.title = title;
        this.content = content;
        this.url = url;
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Doodle doodle = (Doodle) o;

        if (_id != null ? !_id.equals(doodle._id) : doodle._id != null) return false;
        if (_rev != null ? !_rev.equals(doodle._rev) : doodle._rev != null) return false;
        if (title != null ? !title.equals(doodle.title) : doodle.title != null) return false;
        if (content != null ? !content.equals(doodle.content) : doodle.content != null)
            return false;
        return url != null ? url.equals(doodle.url) : doodle.url == null;

    }

    @Override
    public int hashCode() {
        int result = _id != null ? _id.hashCode() : 0;
        result = 31 * result + (_rev != null ? _rev.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
