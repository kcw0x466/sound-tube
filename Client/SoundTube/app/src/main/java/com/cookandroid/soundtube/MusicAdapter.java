package com.cookandroid.soundtube;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import java.lang.Integer;

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
        try {
            return Long.parseLong(musicList.get(position).getId()); // String ID를 long으로 변환
        }
        catch (NumberFormatException e) {
            return position; // 변환 실패 시 리스트의 위치 반환
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_music, parent, false);
        }

        TextView titleView = convertView.findViewById(R.id.title_view);
        TextView durationView = convertView.findViewById(R.id.duration_view);

        MusicInfo music = musicList.get(position);

        int min = music.getLength() / 60;
        int sec = music.getLength() % 60;
        String duration = min + ":" + sec;

        titleView.setText(music.getTitle());
        durationView.setText(duration);

        return convertView;
    }
}
