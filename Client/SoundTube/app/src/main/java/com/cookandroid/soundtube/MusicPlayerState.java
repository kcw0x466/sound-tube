package com.cookandroid.soundtube;

public class MusicPlayerState {
    private String tilte;
    private String url;

    public MusicPlayerState() {
        this.tilte = "";
        this.url = "";
    }

    public void setTilte(String tilte) {
        this.tilte = tilte;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTilte() {
        return tilte;
    }

    public String getUrl() {
        return url;
    }
}
