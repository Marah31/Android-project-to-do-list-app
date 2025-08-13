package com.example.a1183243_1192364_courseproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends Fragment {
    private TextView firstNameTextView, lastNameTextView, emailTextView, passwordTextView;
    private DBHelper dbHelper;
    private int userId;  // You may pass this from the login activity or fetch from shared preferences

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views
        firstNameTextView = view.findViewById(R.id.firstNameTextView);
        lastNameTextView = view.findViewById(R.id.lastNameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        passwordTextView = view.findViewById(R.id.passwordTextView);

        dbHelper = new DBHelper(getActivity());

        // Get the user ID from shared preferences or login activity
        SharedPreferences preferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);  // Get userId (adjust based on how you're handling login)

        if (userId != -1) {
            fetchUserProfile();
        }

        return view;
    }

    private void fetchUserProfile() {
        // Query the database to fetch user data
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        /*String query = "SELECT * FROM " + DBHelper.TABLE_USERS + " WHERE " + DBHelper.COLUMN_ID2 + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            // Fetch user details from the cursor
            String firstName = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_LAST_NAME));
            String email = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EMAIL));
            String password = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PASSWORD));

            // Set the fetched data into the TextViews
            firstNameTextView.setText(firstName);
            lastNameTextView.setText(lastName);
            emailTextView.setText(email);
            passwordTextView.setText(password);

            cursor.close();*/
        }
    }


