package com.example.a1183243_1192364_courseproject;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button saveButton;
    private int userId;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        saveButton = findViewById(R.id.saveButton);

        dbHelper = new DBHelper(this);

        // Get the userId from the intent
        userId = getIntent().getIntExtra("userId", -1);

        // Fetch the current user data and populate the fields
        fetchUserProfile();

        saveButton.setOnClickListener(v -> {
            // Save the updated profile data
            String newEmail = emailEditText.getText().toString();
            String newPassword = passwordEditText.getText().toString();
            updateUserProfile(newEmail, newPassword);
        });
    }

    private void fetchUserProfile() {
        User user = dbHelper.getUserById(userId);
        if (user != null) {
            emailEditText.setText(user.getEmail());
            passwordEditText.setText(user.getPassword());
        }
    }

    private void updateUserProfile(String email, String password) {
        boolean success = dbHelper.updateUserProfile(userId, email, password);
        if (success) {
            Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity and go back to the previous screen
        } else {
            Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
        }
    }
}
