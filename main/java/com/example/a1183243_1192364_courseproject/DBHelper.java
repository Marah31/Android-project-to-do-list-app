package com.example.a1183243_1192364_courseproject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.Calendar;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DUE_DATE = "due_date";
    public static final String COLUMN_DUE_TIME = "due_time";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PRIORITY = "priority";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_CATEGORY = "category";
    private static final String TABLE_USERS = "USERS";
    public static final String COLUMN_REMINDER = "reminder_time";
    // Column names for USERS table
    public static final String COLUMN_ID2 = "id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    // SQL to create USERS table
    public static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " ("
            + COLUMN_ID2 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_EMAIL + " TEXT UNIQUE, "
            + COLUMN_PASSWORD + " TEXT, "
            + "FIRST_NAME TEXT, "
            + "LAST_NAME TEXT);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DBHelper", "Creating tables...");
        String CREATE_TABLE = "CREATE TABLE " + TABLE_TASKS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_DUE_DATE + " TEXT, " +
                COLUMN_DUE_TIME + " TEXT, " +
                COLUMN_PRIORITY + " INTEGER, " +
                COLUMN_STATUS + " TEXT, " +
                COLUMN_CATEGORY + " TEXT," +
                COLUMN_REMINDER + " TEXT)";

        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
        Log.d("DBHelper", "Users table creation SQL: " + CREATE_USERS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
    public SQLiteDatabase openDatabase() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            return db;
        } catch (Exception e) {
            Log.e("DBHelper", "Error opening database", e);
            return null;
        }
    }



    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT EMAIL FROM USERS WHERE EMAIL = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }
    public long addNewUser(String email, String firstName, String lastName, String password) {
        if (isEmailExists(email)) {
            return -1; // Indicate that the email already exists
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Add user data to ContentValues
        values.put("EMAIL", email);
        values.put("FIRST_NAME", firstName);
        values.put("LAST_NAME", lastName);
        values.put("PASSWORD", password);
        // Insert the row into USERS table
        long result = db.insert("USERS", null, values);
        db.close();
        return result;
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USERS, // Table name
                new String[]{COLUMN_ID2, COLUMN_EMAIL, COLUMN_PASSWORD, "FIRST_NAME", "LAST_NAME"}, // Columns to retrieve
                COLUMN_ID2 + " = ?", // WHERE clause
                new String[]{String.valueOf(userId)}, // Selection arguments
                null, null, null); // No grouping, ordering, or having clauses

        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve user data from cursor
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID2));
            String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
            String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
            String firstName = cursor.getString(cursor.getColumnIndex("FIRST_NAME"));
            String lastName = cursor.getString(cursor.getColumnIndex("LAST_NAME"));

            cursor.close();
            return new User(id, firstName, lastName, email, password); // Return user object
        }
        return null; // Return null if no user found
    }

    public boolean updateUserProfile(int userId, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email); // Update email
        values.put(COLUMN_PASSWORD, password); // Update password

        // Update the user's data where the user ID matches
        int rowsUpdated = db.update(
                TABLE_USERS,
                values,
                COLUMN_ID2 + " = ?",
                new String[]{String.valueOf(userId)}
        );

        return rowsUpdated > 0; // Return true if at least one row was updated
    }


    public boolean updateUser(int userId, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);

        int rows = db.update("users", contentValues, "id = ?", new String[]{String.valueOf(userId)});
        return rows > 0;
    }

    public void addTask(String title, String description, String dueDate,String dueTime, int priority, String status, String category, String remainder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_DUE_DATE, dueDate);
        values.put(COLUMN_DUE_TIME, dueTime);
        values.put(COLUMN_PRIORITY, priority);
        values.put(COLUMN_STATUS, status);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_REMINDER, remainder);
        db.insert(TABLE_TASKS, null, values);
        db.close();
    }

    public void updateTask(String taskId, String title, String dueDate,String dueTime, String description, int priority, String category, String status,String remainder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_DUE_DATE, dueDate);
        values.put(COLUMN_DUE_TIME, dueTime);
        values.put(COLUMN_PRIORITY, priority);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_STATUS, status);
        values.put(COLUMN_REMINDER, remainder);
        // Update the task in the database
        int rowsUpdated = db.update(TABLE_TASKS, values, COLUMN_ID + " = ?", new String[]{taskId});

        if (rowsUpdated == 0) {
            Log.e("DBHelper", "Failed to update task");
        } else {
            Log.d("DBHelper", "Task updated successfully with ID: " + taskId);
        }

        db.close();
    }



    public Cursor getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_TASKS, null, null, null, null, null, null);
    }


    public void deleteTask(String taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_TASKS, COLUMN_ID + " = ?", new String[]{taskId});
        if (rowsDeleted == 0) {
            Log.e("DBHelper", "Failed to delete task with ID: " + taskId);
        } else {
            Log.d("DBHelper", "Task deleted successfully with ID: " + taskId);
        }
        db.close();
    }
    public Cursor getTasksForDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TASKS + " WHERE " + COLUMN_DUE_DATE + " = ?", new String[]{date});
    }

    public Cursor getTodayTasks() {
        // Get the current date in the format of "yyyy-MM-dd"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = sdf.format(new Date());

        Log.d("DBHelper", "Querying for tasks due today: " + today); // Debugging the date being used in the query

        // Query to get tasks due today
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKS + " WHERE " + COLUMN_DUE_DATE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{today});

        if (cursor != null && cursor.getCount() == 0) {
            Log.d("DBHelper", "No tasks found for today's date.");
        }

        return cursor;
    }
    public Cursor getTasksByDueDate(String dueDate) {
        SQLiteDatabase db = this.getReadableDatabase();  // Use readable database for queries
        String[] projection = {
                COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION, COLUMN_DUE_DATE,
                COLUMN_PRIORITY, COLUMN_STATUS, COLUMN_CATEGORY
        };

        String selection = COLUMN_DUE_DATE + " = ?";
        String[] selectionArgs = { dueDate };

        return db.query(TABLE_TASKS, projection, selection, selectionArgs, null, null, null);
    }
    public Cursor getCompletedTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM tasks WHERE status = ?";
        return db.rawQuery(query, new String[]{"completed"});
    }
    public Cursor getTasksByDateRange(String startDate, String endDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKS + " WHERE " +
                COLUMN_DUE_DATE + " BETWEEN ? AND ?";
        String[] selectionArgs = {startDate, endDate};
        return db.rawQuery(query, selectionArgs);
    }


    public Cursor getTasksByKeyword(String keyword) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKS + " WHERE " + COLUMN_TITLE + " LIKE ? OR " + COLUMN_DESCRIPTION + " LIKE ?";
        return db.rawQuery(query, new String[]{"%" + keyword + "%", "%" + keyword + "%"});
    }



}