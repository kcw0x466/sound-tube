package com.cookandroid.soundtube;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    ImageView play_pause_Btn, nextBtn, prevBtn;
    TextView musicTitle;
    MusicPlayerState musicPlayerState;
    MusicAdapter musicAdapter;
    MusicInfo nextMusic;
    MediaPlayer mediaPlayer;
    OkHttpClient client;
    List<MusicInfo> musicList;
    DAO Dao;

    String backendAPI_IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        musicPlayerState = new MusicPlayerState(); // 노래 재생기 상태 객체

        // UI 구성 요소들 불러오기
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_Navi_Bar);
        play_pause_Btn = (ImageView) findViewById(R.id.play_pause_btn);
        nextBtn = (ImageView) findViewById(R.id.next_btn);
        prevBtn = (ImageView) findViewById(R.id.prev_btn);
        musicTitle = (TextView) findViewById(R.id.music_title);

        Dao = new DAO(this); // DAO 객체 생성
        musicList = Dao.getAllMusics(); // DB에서 모든 음악 정보들 불러오기
        musicAdapter = new MusicAdapter(this, musicList); // 리스트 뷰 어댑터

        // HTTP 톻신 클라이언트 객체 생성
        backendAPI_IP = "203.253.176.250:8000";
        client = new OkHttpClient();

        // 처음 화면
        setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new MusicList()).commit();
        getSupportActionBar().setTitle("노래");
        getSupportActionBar().setLogo(R.drawable.round_audiotrack);

        // MediaPlayer 인스턴스 생성 및 설정
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );

        // 노래 끝날때 마다 자동으로 다음곡 재생
        mediaPlayer.setOnCompletionListener(mp -> {
            if(musicPlayerState.getCurrentMusicIndex() < musicList.size() && musicPlayerState.getCurrentMode() == musicPlayerState.MODE_MUSIC_LIST) {
                try {
                    nextMusic = musicList.get(musicPlayerState.getCurrentMusicIndex() + 1);
                    Toast.makeText(getApplicationContext(), "Title: " + nextMusic.getTitle(), Toast.LENGTH_SHORT).show();
                    musicTitle.setText(nextMusic.getTitle());
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "전체 재생목록 재생 완료", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource("http://" + backendAPI_IP + "/stream?url=https://www.youtube.com/watch?v=" + nextMusic.getId());
                    mediaPlayer.prepareAsync();  // 비동기적으로 준비
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            // 준비 완료 후 오디오 시작
                            mediaPlayer.start();
                            play_pause_Btn.setImageResource(R.drawable.round_pause);
                            musicPlayerState.setPlayerState(musicPlayerState.PLAYING);
                            musicPlayerState.setCurrentMode(musicPlayerState.MODE_MUSIC_LIST);
                            musicPlayerState.setCurrentMusicIndex(musicList.indexOf(nextMusic));
                        }
                    });
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error loading audio", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 하단 네비게이션바 화면 전환
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
        // 재생 버튼
        play_pause_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(musicPlayerState.getPlayerState() == musicPlayerState.PLAYING) {
                    play_pause_Btn.setImageResource(R.drawable.round_play_arrow);
                    musicPlayerState.setPlayerState(musicPlayerState.PAUSING);
                    mediaPlayer.pause();
                }
                else if(musicPlayerState.getPlayerState() == musicPlayerState.PAUSING) {
                    play_pause_Btn.setImageResource(R.drawable.round_pause);
                    musicPlayerState.setPlayerState(musicPlayerState.PLAYING);
                    mediaPlayer.start();
                }
            }
        });

        // 다음 곡 재생 버튼 (인덱스 순서 버그 있음 -> 내 생각에 노래 로딩이 다되면 인덱스가 설정되서 로딩되기전에 연속으로 클릭해서 그런듯)
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(musicPlayerState.getCurrentMusicIndex() < musicList.size() && musicPlayerState.getCurrentMode() == musicPlayerState.MODE_MUSIC_LIST) {
                    MusicInfo nextMusic = musicList.get(musicPlayerState.getCurrentMusicIndex() + 1); // 배열 인덱스 범위 초과 버그 있음
                    Toast.makeText(getApplicationContext(), "Title: " + nextMusic.getTitle(), Toast.LENGTH_SHORT).show();

                    musicTitle.setText(nextMusic.getTitle());
                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource("http://" + backendAPI_IP + "/stream?url=https://www.youtube.com/watch?v=" + nextMusic.getId());
                        mediaPlayer.prepareAsync();  // 비동기적으로 준비
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                // 준비 완료 후 오디오 시작
                                mediaPlayer.start();
                                play_pause_Btn.setImageResource(R.drawable.round_pause);
                                musicPlayerState.setPlayerState(musicPlayerState.PLAYING);
                                musicPlayerState.setCurrentMode(musicPlayerState.MODE_MUSIC_LIST);
                                musicPlayerState.setCurrentMusicIndex(musicList.indexOf(nextMusic));
                            }
                        });
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error loading audio", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // 이전 곡 재생 버튼 (인덱스 순서 버그 있음 -> 내 생각에 노래 로딩이 다되면 인덱스가 설정되서 로딩되기전에 연속으로 클릭해서 그런듯)
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(musicPlayerState.getCurrentMusicIndex() > 0 && musicPlayerState.getCurrentMode() == musicPlayerState.MODE_MUSIC_LIST) {
                    MusicInfo prevMusic = musicList.get(musicPlayerState.getCurrentMusicIndex() - 1);
                    Toast.makeText(getApplicationContext(), "Title: " + prevMusic.getTitle(), Toast.LENGTH_SHORT).show();

                    musicTitle.setText(prevMusic.getTitle());
                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource("http://" + backendAPI_IP + "/stream?url=https://www.youtube.com/watch?v=" + prevMusic.getId());
                        mediaPlayer.prepareAsync();  // 비동기적으로 준비
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                // 준비 완료 후 오디오 시작
                                mediaPlayer.start();
                                play_pause_Btn.setImageResource(R.drawable.round_pause);
                                musicPlayerState.setPlayerState(musicPlayerState.PLAYING);
                                musicPlayerState.setCurrentMode(musicPlayerState.MODE_MUSIC_LIST);
                                musicPlayerState.setCurrentMusicIndex(musicList.indexOf(prevMusic));
                            }
                        });
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error loading audio", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // 상단바 메뉴 불러오기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.appbar_action, menu);
        return true;
    }

    // 상단바 메뉴 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add){
            musicAddDialog();
        }
        else if (item.getItemId() == R.id.action_delete) {
            musicDeleteDialog();
        }

        return super.onOptionsItemSelected(item);

    }

    // 노래 추가 다이얼로그 (버그: Toast를 UI 스레드에서 처리안해서 앱 강제 종료됨)
    public void musicAddDialog() {
        // URL 입력을 위한 EditText 생성
        EditText urlEditText = new EditText(this);
        urlEditText.setHint("유튜브 영상 URL 입력");

        // AlertDialog 생성
        new AlertDialog.Builder(this)
                .setView(urlEditText) // EditText를 대화 상자에 추가
                .setPositiveButton("확인", (dialog, which) -> {
                    String url = urlEditText.getText().toString().trim();
                    Request request;
                    try {
                        request = new Request.Builder()
                            .url("http://" + backendAPI_IP + "/getInfo?url=" + url)
                            .get()
                            .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                Toast.makeText(getApplicationContext(), "Error Request Failed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                if (response.isSuccessful() && response.body() != null) {
                                    // 성공적으로 응답 처리
                                    try{
                                        String responseBody = response.body().string();
                                        Gson gson = new Gson();
                                        MusicInfo musicInfo = gson.fromJson(responseBody, MusicInfo.class);
                                        if (musicInfo.getErr() == 0) {
                                            Dao.insertMusic(musicInfo); // 노래 추가

                                            // 리스트 뷰 새로고침 메서드를 UI 스레드에서 호출
                                            runOnUiThread(() -> {
                                                refreshList();
                                            });

                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Error: 유튜브 영상을 찾지 못했습니다", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Error Network", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                    catch (Exception e) {
                        Toast.makeText(this, "Error Request Failed", Toast.LENGTH_SHORT).show();
                    }

                })
                .setNegativeButton("취소", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // 노래 삭제 다이얼로그
    public void musicDeleteDialog() {
        String[] musicTitles = new String[musicList.size()];
        for (int i = 0; i < musicList.size(); i++) {
            musicTitles[i] = musicList.get(i).getTitle();
        }

        boolean[] selectedMusics = new boolean[musicList.size()]; // 항목 선택 상태 저장
        List<MusicInfo> selectedList = new ArrayList<>(); // 선택된 항목 리스트

        // AlertDialog 생성
        new AlertDialog.Builder(this)
                .setTitle("삭제할 노래들을 선택하세요")
                .setMultiChoiceItems(musicTitles, selectedMusics, (dialog, which, isChecked) -> {
                    // 항목 선택/해제
                    if (isChecked) {
                        selectedList.add(musicList.get(which)); // 선택 항목 추가
                    } else {
                        selectedList.remove(musicList.get(which)); // 선택 항목 제거
                    }
                })
                .setPositiveButton("확인", (dialog, which) -> {
                    // 선택 결과 처리
                    for(MusicInfo music : selectedList){
                        Log.d("Delete", "Title: " + music.getTitle() + ", Duration: " + music.getLength());
                        Dao.deleteMusic(music);
                    }
                    refreshList();
                })
                .setNegativeButton("취소", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // 리스트 새로고침
    public void refreshList() {
        List<MusicInfo> newMusicList = Dao.getAllMusics();
        musicList.clear();
        musicList.addAll(newMusicList);
        musicAdapter.refreshData();
    }
}