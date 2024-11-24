package com.cookandroid.soundtube;

public class MusicPlayerState {
    final int STOPPING = 0;
    final int PLAYING = 1;
    final int PAUSING = 2;
    final int MODE_MUSIC_LIST = 1;
    final int MODE_QUICK_PLAY = 2;
    private int currentMode;
    private int playerState;
    private int currentMusicIndex;

    public MusicPlayerState() {
        this.currentMode = 0;
        this.playerState = STOPPING;
        this.currentMusicIndex = 0;
    }

    public void setPlayerState(int playerState) {
        this.playerState = playerState;
    }
    public void setCurrentMode(int currentMode) {
        this.currentMode = currentMode;
    }
    public void setCurrentMusicIndex(int index) { this.currentMusicIndex = index; }

    public int getPlayerState() {
        return playerState;
    }
    public int getCurrentMode() {
        return currentMode;
    }
    public int getCurrentMusicIndex() { return currentMusicIndex; }
}
