package com.example.ktb1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDB.db";
    private static final int DATABASE_VERSION = 2; // Tăng version lên để onUpgrade được gọi

    private static final String TABLE_NAME = "users";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";
    private static final String COL_ROLE = "role";

    // Appointment table
    private static final String APPOINTMENT_TABLE = "Appointments";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng người dùng
        String createUserTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_USERNAME + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT, " +
                COL_ROLE + " TEXT)";
        db.execSQL(createUserTable);

        // Tạo bảng cuộc hẹn
        String createAppointmentTable = "CREATE TABLE " + APPOINTMENT_TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "doctor TEXT, " +
                "patient TEXT, " +
                "date TEXT, " +
                "time TEXT)";
        db.execSQL(createAppointmentTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + APPOINTMENT_TABLE);
        onCreate(db);
    }

    public boolean registerUser(String name, String username, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);
        values.put(COL_ROLE, role);
        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public String getRole(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT role FROM " + TABLE_NAME + " WHERE username=? AND password=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        if (cursor.moveToFirst()) {
            String role = cursor.getString(0);
            cursor.close();
            return role;
        }
        cursor.close();
        return null;
    }
}
