package com.cookandroid.soundtube;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;




public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_Navi_Bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("노래");
        getSupportActionBar().setLogo(R.drawable.round_audiotrack);

        // 처음 화면
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new MusicList()).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.menu_music){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MusicList()).commit();
                    getSupportActionBar().setTitle("노래");
                    getSupportActionBar().setLogo(R.drawable.round_audiotrack);
                }
                else if (item.getItemId() == R.id.menu_playlist) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new PlayList()).commit();
                    getSupportActionBar().setTitle("재생목록");
                    getSupportActionBar().setLogo(R.drawable.round_playlist_play);
                }
                else if (item.getItemId() == R.id.menu_quick_play) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new QuickPlay()).commit();
                    getSupportActionBar().setTitle("퀵 플레이");
                    getSupportActionBar().setLogo(R.drawable.round_music_video_24);
                }
                return true;
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


