package com.cookandroid.soundtube;

public class MusicInfo {
    private int err;
    private String id;
    private String title;
    private int length;

    public int getErr() {
        return err;
    }

    public int getLength() {
        return length;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setErr(int err) { this.err = err; }

    public void setLength(int length) {
        this.length = length;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
