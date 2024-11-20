package com.cookandroid.soundtube;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class QuickPlay extends Fragment {
    EditText inputURL;
    Button musicLoadBtn;
    View view;
    MainActivity mainActivity;

    Request request;

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
                    request = new Request.Builder()
                            .url("http://10.0.2.2:8000/getInfo?url=" + inputURL.getText().toString())
                            .build();
                }
                catch (Exception e) {
                    Toast.makeText(mainActivity, "Error Request Failed", Toast.LENGTH_SHORT).show();
                }

                mainActivity.client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Toast.makeText(mainActivity, "Error Request Failed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful() && response.body() != null) {
                            // 성공적으로 응답 처리
                            String responseBody = response.body().string();
                            Gson gson = new Gson();
                            MusicInfo musicInfo = gson.fromJson(responseBody, MusicInfo.class);
                            if (musicInfo.getErr() == 1) {
                                Toast.makeText(mainActivity, "Error loading Music Title", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                mainActivity.musicTitle.setText(musicInfo.getTitle());
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

                        }
                        else {
                            Toast.makeText(mainActivity, "Error Network", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return view;
    }
}