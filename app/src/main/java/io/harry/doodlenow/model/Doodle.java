package io.harry.doodlenow.model;

public class Doodle {
    public final String _id;
    public final String title;
    public final String content;

    public Doodle(String content) {
        this._id = null;
        this.title = null;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Doodle doodle = (Doodle) o;

        if (_id != null ? !_id.equals(doodle._id) : doodle._id != null) return false;
        if (title != null ? !title.equals(doodle.title) : doodle.title != null) return false;
        return content != null ? content.equals(doodle.content) : doodle.content == null;

    }

    @Override
    public int hashCode() {
        int result = _id != null ? _id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }
}
