package com.cookandroid.soundtube;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class MusicAdapter extends BaseAdapter {
    private final Context context;
    private final List<MusicInfo> musicList;

    public MusicAdapter(Context context, List<MusicInfo> musicList) {
        this.context = context;
        this.musicList = musicList;
    }

    @Override
    public int getCount() {
        return musicList.size();
    }

    @Override
    public Object getItem(int position) {
        return musicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_music, parent, false);
        }

        TextView titleView = convertView.findViewById(R.id.title_view);
        TextView durationView = convertView.findViewById(R.id.duration_view);

        MusicInfo music = (MusicInfo) getItem(position);
        Log.d("MusicAdapter", "Title: " + music.getTitle() + ", Duration: " + music.getLength());

        int min = music.getLength() / 60;
        int sec = music.getLength() % 60;
        String duration = min + ":" + sec;

        titleView.setText(music.getTitle());
        durationView.setText(duration);

        return convertView;
    }
}
