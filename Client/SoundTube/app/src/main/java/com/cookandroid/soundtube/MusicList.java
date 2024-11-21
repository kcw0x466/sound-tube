package com.cookandroid.soundtube;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;


public class MusicList extends Fragment {
    private MusicAdapter musicAdapter;
    ListView listView;
    MainActivity mainActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);
        listView = (ListView) view.findViewById(R.id.music_list_view);

        mainActivity = (MainActivity) getActivity();

        musicAdapter = new MusicAdapter(requireContext(), mainActivity.musicList);
        listView.setAdapter(musicAdapter);

        listView.setOnItemClickListener((parent, listview, position, id) -> {
            MusicInfo clickedMusic = mainActivity.musicList.get(position);
            Toast.makeText(requireContext(), "Title: " + clickedMusic.getTitle(), Toast.LENGTH_SHORT).show();

            mainActivity.musicTitle.setText(clickedMusic.getTitle());
            try {
                mainActivity.mediaPlayer.reset();
                mainActivity.mediaPlayer.setDataSource("http://10.0.2.2:8000/stream?url=https://www.youtube.com/watch?v=" + clickedMusic.getId());
                mainActivity.mediaPlayer.prepareAsync();  // 비동기적으로 준비
                mainActivity.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        // 준비 완료 후 오디오 시작
                        mainActivity.mediaPlayer.start();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(mainActivity, "Error loading audio", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainActivity.Dao.close(); // DAO 닫기
    }
}