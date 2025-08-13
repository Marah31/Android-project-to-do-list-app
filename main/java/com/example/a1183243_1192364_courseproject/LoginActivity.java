package com.example.a1183243_1192364_courseproject;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private EditText emailField;
    private EditText passwordField;
    private CheckBox rememberMeCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);
        Button loginButton = findViewById(R.id.loginButton);

        dbHelper = new DBHelper(this);

        // Load saved email from SharedPreferences
        loadRememberedEmail();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                if (validateLogin(email, password)) {
                    // If login is successful, update the SharedPreferences
                    SharedPrefManager.getInstance(LoginActivity.this).setLoggedIn(true);  // Set logged in status here

                    if (rememberMeCheckBox.isChecked()) {
                        saveEmailToPreferences(email);
                    } else {
                        clearSavedEmail();
                    }

                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                    // Navigate to MainActivity
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

                    // Delay finish to ensure MainActivity is launched
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish(); // Finish LoginActivity after MainActivity is started
                        }
                    }, 500); // Delay for 500ms
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid email or password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void saveEmailToPreferences(String email) {
        SharedPrefManager.getInstance(this).saveEmail(email);
    }

    public boolean validateLogin(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM USERS WHERE EMAIL = ? AND PASSWORD = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        boolean isValidUser = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValidUser;
    }


    private void loadRememberedEmail() {
        String rememberedEmail = SharedPrefManager.getInstance(this).getSavedEmail();
        if (rememberedEmail != null) {
            emailField.setText(rememberedEmail);
            rememberMeCheckBox.setChecked(true);
        }
    }

    private void clearSavedEmail() {
        SharedPrefManager.getInstance(this).clearSavedEmail();
    }
}
