package com.example.a1183243_1192364_courseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        EditText Email = findViewById(R.id.Email);
        EditText FirstName = findViewById(R.id.FirstName);
        EditText LastName = findViewById(R.id.LastName);
        EditText Password = findViewById(R.id.Password);
        EditText ConfirmPassword = findViewById(R.id.ConfirmPassword);
        Button btnRegister = findViewById(R.id.btnRegister);

        dbHelper = new DBHelper(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;
                // Email validation
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String email = Email.getText().toString().trim();
                if (!Pattern.matches(emailPattern, email)) {
                    //Email.setBackgroundColor(Color.RED);
                    Email.setError("Enter valid email format!");
                    isValid = false;
                }

                // First name validation
                String firstName = FirstName.getText().toString().trim();
                if (firstName.length() < 5 || firstName.length() > 20) {
                    //FirstName.setBackgroundColor(Color.RED);
                    FirstName.setError("Minimum 5 characters and maximum 20 characters!");
                    isValid = false;
                }

                // Last name validation
                String lastName = LastName.getText().toString().trim();
                if (lastName.length() < 5 || lastName.length() > 20) {
                    //LastName.setBackgroundColor(Color.RED);
                    LastName.setError("Minimum 5 characters and maximum 20 characters!");
                    isValid = false;
                }

                // Password validation
                String password = Password.getText().toString();
                String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,12}$";
                if (!Pattern.matches(passwordPattern, password)) {
                    // Password.setBackgroundColor(Color.RED);
                    Password.setError("Minimum 6 characters and maximum 12 characters. It must contain at \n" +
                            "least one number, one lowercase letter, and one uppercase letter! ");
                    isValid = false;
                }

                // Confirm password validation
                String confirmPassword = ConfirmPassword.getText().toString();
                if (!password.equals(confirmPassword)) {
                    // ConfirmPassword.setBackgroundColor(Color.RED);
                    ConfirmPassword.setError("Wrong Confirmed Password");

                    isValid = false;
                }

                // Final validation check
                if (isValid) {
                    long result = dbHelper.addNewUser(email, firstName, lastName, password);

                    if (result == -1) {
                        // If -1, it means the email already exists
                        Email.setError("Email already registered");
                        Toast.makeText(SignupActivity.this, "Email already exists. Please use a different email.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignupActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        });

    }
}
