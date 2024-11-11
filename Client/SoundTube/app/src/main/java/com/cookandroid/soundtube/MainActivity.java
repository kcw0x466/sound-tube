package com.cookandroid.soundtube;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.media.AudioAttributes;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button playBtn, pauseBtn, stopBtn;
    MediaPlayer mediaPlayer;
    String url = "http://203.253.176.250:8000/stream?url=https://www.youtube.com/watch?v=Ocq3Y6DH4D0"; // 스트리밍할 오디오 URL 입력

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // MediaPlayer 인스턴스 생성 및 설정
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );

        // 버튼 초기화
        playBtn = findViewById(R.id.play_button);
        pauseBtn = findViewById(R.id.pause_button);
        stopBtn = findViewById(R.id.stop_button);

        // Play 버튼 클릭 시
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepareAsync();  // 비동기적으로 준비
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            // 준비 완료 후 오디오 시작
                            mediaPlayer.start();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error loading audio", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Pause 버튼 클릭 시
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();  // 음성 일시정지
                }
            }
        });

        // Stop 버튼 클릭 시
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();  // 음성 정지
                    mediaPlayer.reset(); // MediaPlayer 리셋 (다시 사용할 수 있도록)
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.release();  // MediaPlayer 리소스 해제
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();  // MediaPlayer 리소스 해제
        }
    }
}
