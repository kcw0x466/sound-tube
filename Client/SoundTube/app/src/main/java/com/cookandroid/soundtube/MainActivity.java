package com.cookandroid.soundtube;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;




public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    ImageView play_pause_Btn, nextBtn, prenBtn;
    MusicPlayerState musicPlayerState;
    MediaPlayer mediaPlayer;

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

        musicPlayerState = new MusicPlayerState();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_Navi_Bar);

        play_pause_Btn = (ImageView) findViewById(R.id.play_pause_btn);
        nextBtn = (ImageView) findViewById(R.id.next_btn);
        prenBtn = (ImageView) findViewById(R.id.prev_btn);

        // 처음 화면
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new MusicList()).commit();
        getSupportActionBar().setTitle("노래");
        getSupportActionBar().setLogo(R.drawable.round_audiotrack);

        // 하단 네비게이션바 세팅
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.menu_music){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MusicList()).commit();
                    getSupportActionBar().setTitle("노래");
                    getSupportActionBar().setLogo(R.drawable.round_audiotrack);
                }
                else if (item.getItemId() == R.id.menu_quick_play) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new QuickPlay()).commit();
                    getSupportActionBar().setTitle("퀵 플레이");
                    getSupportActionBar().setLogo(R.drawable.round_music_video_24);
                }
                return true;
            }
        });

        // 음악 컨트롤러 버튼 세팅
        play_pause_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.appbar_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_settings1:
//                Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
//                return true;
//
//        }
        return super.onOptionsItemSelected(item);
    }
}


