package com.example.a1183243_1192364_courseproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TaskDetailActivity extends AppCompatActivity {

    private TextView titleTextView, descriptionTextView, dueDateTextView, statusTextView, categoryTextView, priorityTextView,reminderTextView;
    private DBHelper dbHelper;
    private String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        // Initialize TextViews to display task details
        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        dueDateTextView = findViewById(R.id.dueDateTextView);
        statusTextView = findViewById(R.id.statusTextView);
        categoryTextView = findViewById(R.id.categoryTextView);
        priorityTextView = findViewById(R.id.priorityTextView);
        reminderTextView = findViewById(R.id.reminderTextView);

        Button edit = findViewById(R.id.edit_task_button);
        Button delete = findViewById(R.id.delete_task_button);
        Button goBack = findViewById(R.id.return_button);

        // Get the task details passed from the previous activity
        Intent intent = getIntent();
        taskId = intent.getStringExtra("taskId");
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String dueDate = intent.getStringExtra("dueDate");
        String status = intent.getStringExtra("status");
        String category = intent.getStringExtra("category");
        int priority = intent.getIntExtra("priority", 0); // Retrieve priority as an integer
        String reminder = intent.getStringExtra("reminder");

        // Display the task details in the TextViews
        titleTextView.setText(title);
        descriptionTextView.setText(description);
        dueDateTextView.setText(dueDate);
        statusTextView.setText(status);
        categoryTextView.setText(category);
        priorityTextView.setText(String.valueOf(priority)); // Convert priority to String and set it
        reminderTextView.setText(reminder);

        dbHelper = new DBHelper(this);

        goBack.setOnClickListener(v -> {
            Intent mainIntent = new Intent(TaskDetailActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        });

        edit.setOnClickListener(v -> {
            int updatedPriority = 3;
            Intent editIntent = new Intent(TaskDetailActivity.this, EditTaskActivity.class);
            editIntent.putExtra("taskId", taskId); // Pass taskId
            editIntent.putExtra("title", title);
            editIntent.putExtra("description", description);
            editIntent.putExtra("dueDate", dueDate);
            editIntent.putExtra("priority", updatedPriority);
            editIntent.putExtra("category", category);
            editIntent.putExtra("status", status);
            editIntent.putExtra("status", reminder);
            startActivity(editIntent);
        });



        delete.setOnClickListener(v -> {
            new AlertDialog.Builder(TaskDetailActivity.this)
                    .setTitle("Delete Task")
                    .setMessage("Are you sure you want to delete this task?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        if(taskId != null) {
                            dbHelper.deleteTask(taskId);
                            Toast.makeText(this, "Task deleted successfully", Toast.LENGTH_SHORT).show();
                            Intent mainIntent = new Intent(TaskDetailActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();// Close TaskDetailActivity
                        }else {
                            Log.e("TaskDetailActivity", "Task ID is null, cannot delete task.");
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });


    }
}


