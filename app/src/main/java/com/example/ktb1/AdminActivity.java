package com.example.ktb1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;


public class AdminActivity extends AppCompatActivity {

    ListView lvUsers;
    DatabaseHelper dbHelper;
    ArrayAdapter<String> adapter;
    ArrayList<String> userList = new ArrayList<>();
    ArrayList<Integer> userIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        lvUsers = findViewById(R.id.lvUsers);
        dbHelper = new DatabaseHelper(this);

        loadUsers();

        lvUsers.setOnItemLongClickListener((parent, view, position, id) -> {
            int userId = userIds.get(position);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL("DELETE FROM Users WHERE id = ?", new Object[]{userId});
            Toast.makeText(this, "Đã xóa tài khoản", Toast.LENGTH_SHORT).show();
            loadUsers();
            return true;
        });
    }

    private void loadUsers() {
        userList.clear();
        userIds.clear();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, name, role FROM Users", null);
        while (cursor.moveToNext()) {
            userIds.add(cursor.getInt(0));
            userList.add(cursor.getString(1) + " - " + cursor.getString(2));
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        lvUsers.setAdapter(adapter);
    }
}
