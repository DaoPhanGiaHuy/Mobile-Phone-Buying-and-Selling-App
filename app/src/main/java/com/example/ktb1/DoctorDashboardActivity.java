package com.example.ktb1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DoctorDashboardActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    LinearLayout todayScheduleLayout;
    String currentDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard); // Đảm bảo file XML đúng tên

        todayScheduleLayout = findViewById(R.id.todaySchedule);
        dbHelper = new DatabaseHelper(this);

        // Nhận username từ LoginActivity
        Intent intent = getIntent();
        currentDoctor = intent.getStringExtra("username");

        if (currentDoctor == null) {
            Toast.makeText(this, "Không xác định được Doctor!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadAppointments();
    }

    private void loadAppointments() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT patient, date, time FROM Appointments WHERE doctor = ?", new String[]{currentDoctor});

        if (cursor.moveToFirst()) {
            do {
                String patient = cursor.getString(cursor.getColumnIndexOrThrow("patient"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));

                TextView textView = new TextView(this);
                textView.setText("🕑 " + time + " - " + patient + " (" + date + ")");
                textView.setPadding(0, 8, 0, 8);
                todayScheduleLayout.addView(textView);
            } while (cursor.moveToNext());
        } else {
            TextView emptyView = new TextView(this);
            emptyView.setText("Không có lịch hẹn nào.");
            todayScheduleLayout.addView(emptyView);
        }

        cursor.close();
        db.close();
    }
}
