package com.cookandroid.soundtube;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DAO {
    private final SQLiteDatabase database;

    public DAO(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.copyDatabase();
        database = dbHelper.openDatabase(); // DatabaseHelper에서 기존 데이터베이스를 열기
    }

    // 음악 추가
    public long insertMusic(MusicInfo music) {
        ContentValues values = new ContentValues();
        values.put("id", music.getId());
        values.put("title", music.getTitle());
        values.put("length", music.getLength());
        return database.insert("music", null, values); // 새 레코드 추가
    }

    // 모든 음악 조회
    public List<MusicInfo> getAllMusics() {
        List<MusicInfo> musicList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM music", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                MusicInfo music = new MusicInfo();
                music.setId(cursor.getString(cursor.getColumnIndexOrThrow("id")));
                music.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                music.setLength(cursor.getInt(cursor.getColumnIndexOrThrow("length")));
                musicList.add(music);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return musicList;
    }

    // 음악 삭제
    public int deleteMusic(MusicInfo music) {
        return database.delete(
                "music",
                "id = ?",
                new String[]{String.valueOf(music.getId())}
        );
    }

    // 데이터베이스 닫기
    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}
