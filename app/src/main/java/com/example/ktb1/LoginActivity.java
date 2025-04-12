package com.example.ktb1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnLogin;
    TextView txtRegister;
    Spinner spinnerRole;

    String[] roles = {"Admin", "Doctor", "Patient"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);
        spinnerRole = findViewById(R.id.spinnerRole);

        // Gắn dữ liệu cho Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        // Khởi tạo database
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Xử lý nút đăng nhập
        btnLogin.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String selectedRole = spinnerRole.getSelectedItem().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            String roleInDB = dbHelper.getRole(username, password);
            if (roleInDB != null && roleInDB.equals(selectedRole)) {
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                Intent intent;
                switch (roleInDB) {
                    case "Admin":
                        intent = new Intent(this, AdminActivity.class);
                        break;
                    case "Doctor":
                        intent = new Intent(this, DoctorDashboardActivity.class);
                        break;
                    case "Patient":
                        intent = new Intent(this, PatientDashboardActivity.class);
                        break;
                    default:
                        Toast.makeText(this, "Vai trò không hợp lệ", Toast.LENGTH_SHORT).show();
                        return;
                }

                intent.putExtra("username", username);

                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Sai thông tin đăng nhập hoặc vai trò", Toast.LENGTH_SHORT).show();
            }
        });

        // Điều hướng tới màn hình đăng ký
        txtRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
