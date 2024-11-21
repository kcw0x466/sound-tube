package com.cookandroid.soundtube;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper {
    private static final String DB_NAME = "sound_tube.db"; // assets에 있는 파일 이름
    private static final String DB_PATH = "/data/data/"; // 앱의 내부 저장소 경로
    private final Context context;

    public DatabaseHelper(Context context) {
        this.context = context;
    }

    public void copyDatabase() {
        try {
            String outFileName = DB_PATH + context.getPackageName() + "/databases/" + DB_NAME;

            // 데이터베이스가 이미 복사되었는지 확인
            File dbFile = new File(outFileName);
            if (dbFile.exists()) {
                Log.d("DatabaseHelper", "Database already exists");
                return;
            }

            // 데이터베이스 디렉토리 생성
            File dbDirectory = new File(DB_PATH + context.getPackageName() + "/databases/");
            if (!dbDirectory.exists()) {
                dbDirectory.mkdirs();
            }

            // assets에서 데이터베이스 복사
            InputStream input = context.getAssets().open(DB_NAME);
            OutputStream output = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            input.close();

            Log.d("DatabaseHelper", "Database copied successfully");

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DatabaseHelper", "Error copying database: " + e.getMessage());
        }
    }

    public SQLiteDatabase openDatabase() {
        String dbPath = DB_PATH + context.getPackageName() + "/databases/" + DB_NAME;
        return SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }
}