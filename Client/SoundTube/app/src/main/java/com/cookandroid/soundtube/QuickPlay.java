package com.cookandroid.soundtube;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class QuickPlay extends Fragment {
    EditText inputURL;
    Button musicLoadBtn;
    View view;
    MainActivity mainActivity;
//    public QuickPlay() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_quick_play, container, false);
        inputURL = view.findViewById(R.id.input_yt_link);
        musicLoadBtn = view.findViewById(R.id.music_load_btn);
        mainActivity = (MainActivity) getActivity();

//        if (mainActivity != null) {
//        }


        musicLoadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mainActivity.mediaPlayer.reset();
                    mainActivity.mediaPlayer.setDataSource("http://10.0.2.2:8000/stream?url=" + inputURL.getText().toString());
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
            }
        });

        return view;
    }
}