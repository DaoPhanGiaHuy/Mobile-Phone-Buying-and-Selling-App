package com.example.ktb1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PatientDashboardActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    LinearLayout upcomingAppointments;
    TextView nextAppointment;
    String currentPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        // Gán các View
        upcomingAppointments = findViewById(R.id.upcomingAppointments);
        nextAppointment = findViewById(R.id.nextAppointment);
        dbHelper = new DatabaseHelper(this);

        // Nhận username từ LoginActivity
        Intent intent = getIntent();
        currentPatient = intent.getStringExtra("username");

        if (currentPatient == null) {
            Toast.makeText(this, "Không xác định được Patient!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadAppointments();
    }

    private void loadAppointments() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT date, time, doctor FROM Appointments WHERE patient = ? ORDER BY date, time",
                new String[]{currentPatient}
        );

        if (cursor.moveToFirst()) {
            // Hiển thị cuộc hẹn gần nhất
            String next = "🩺 Bác sĩ: " + cursor.getString(2) + "\n📅 " + cursor.getString(0) + " - 🕒 " + cursor.getString(1);
            nextAppointment.setText(next);

            // Hiển thị các cuộc hẹn tiếp theo
            do {
                String item = "🩺 Bác sĩ: " + cursor.getString(2)
                        + "\n📅 " + cursor.getString(0)
                        + " - 🕒 " + cursor.getString(1);
                TextView tv = new TextView(this);
                tv.setText(item);
                tv.setPadding(0, 10, 0, 10);
                upcomingAppointments.addView(tv);
            } while (cursor.moveToNext());
        } else {
            nextAppointment.setText("Không có lịch hẹn nào.");
        }

        cursor.close();
    }
}
