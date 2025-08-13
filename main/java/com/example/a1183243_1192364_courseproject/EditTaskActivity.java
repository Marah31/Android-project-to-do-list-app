package com.example.a1183243_1192364_courseproject;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditTaskActivity extends AppCompatActivity {

    private EditText editTitle, editDescription;
    private TextView showDueDate, showDueTime;
    private Spinner spinnerPriority, spinnerCategory;
    private Button editDate, editTime, saveButton;
    private CheckBox statusCheckBox;
    private DBHelper dbHelper;
    private String taskId;
    private String originalDueDate = "", originalDueTime = "", notification= "";
    private Spinner spinnerNotificationTimes;
    //private String dueDate = "", dueTime = "", notification= "";
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        // Initialize Views
        editTitle = findViewById(R.id.title2);
        editDescription = findViewById(R.id.description2);
        showDueDate = findViewById(R.id.showDate);
        showDueTime = findViewById(R.id.showTime);
        spinnerPriority = findViewById(R.id.priority2);
        spinnerCategory = findViewById(R.id.category2);
        statusCheckBox = findViewById(R.id.completion2);
        editDate = findViewById(R.id.setDate2);
        editTime = findViewById(R.id.setTime2); // Add a button for time selection
        spinnerNotificationTimes = findViewById(R.id.spinner_notification_times);
        saveButton = findViewById(R.id.save2_button);

        dbHelper = new DBHelper(this);

        // Get task details from Intent
        Intent intent = getIntent();
        taskId = intent.getStringExtra("taskId");
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        int priority = intent.getIntExtra("priority", 3); // Default to low priority
        String category = intent.getStringExtra("category");
        String status = intent.getStringExtra("status");
        originalDueDate = intent.getStringExtra("dueDate");
        originalDueTime = intent.getStringExtra("dueTime");

        // Format the original date and time
        String formattedDueDate = formatDate(originalDueDate);
        String formattedDueTime = originalDueTime;

        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(this,
                R.array.priority_array, android.R.layout.simple_spinner_item);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(priorityAdapter);
        spinnerPriority.setSelection(priority - 1); // Default priority selection

        editTitle.setText(title);
        editDescription.setText(description);
        showDueDate.setText(formattedDueDate);
        showDueTime.setText(formattedDueTime);

        // Set up the Category Spinner
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        spinnerCategory.setAdapter(categoryAdapter);
        spinnerCategory.setSelection(categoryAdapter.getPosition(category));

        statusCheckBox.setChecked("completed".equals(status));
        //reminderCheckBox.setChecked("on".equals(reminderStatus));

        // Date Picker Logic
        editDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, yearSelected, monthSelected, daySelected) -> {
                // Get the current date and time
                Calendar currentDate = Calendar.getInstance();
                Calendar selectedDate = Calendar.getInstance();

                // Set selected date
                selectedDate.set(yearSelected, monthSelected, daySelected);

                // Validate if selected date is in the past
                if (selectedDate.before(currentDate)) {
                    Toast.makeText(this, "The due date cannot be in the past. Please select a valid date.", Toast.LENGTH_SHORT).show();
                } else {
                    // Save valid date
                    selectedYear = yearSelected;
                    selectedMonth = monthSelected;
                    selectedDay = daySelected;

                    originalDueDate = daySelected + "/" + (monthSelected + 1) + "/" + yearSelected;
                    showDueDate.setText(originalDueDate);
                }
            }, year, month, day);
            datePickerDialog.show();
        });

        // Time Picker Logic
        editTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourSelected, minuteSelected) -> {
                // Get the current date and time
                Calendar currentDateTime = Calendar.getInstance();
                Calendar selectedDateTime = Calendar.getInstance();

                // Set selected date and time
                selectedDateTime.set(selectedYear, selectedMonth, selectedDay, hourSelected, minuteSelected);


                // Save valid time
                selectedHour = hourSelected;
                selectedMinute = minuteSelected;

                originalDueTime = String.format(Locale.getDefault(), "%02d:%02d", hourSelected, minuteSelected);
                showDueTime.setText(originalDueTime);

            }, hour, minute, true);
            timePickerDialog.show();
        });
        /////////
        // Create an array of options
        String[] notificationTimes = {"no notification","A day before", "An hour before"};

        // Set up ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, notificationTimes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attach adapter to Spinner
        spinnerNotificationTimes.setAdapter(adapter);

        // Set a listener for Spinner item selection
        spinnerNotificationTimes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTime = parent.getItemAtPosition(position).toString();
                Toast.makeText(EditTaskActivity.this, "Selected: " + selectedTime, Toast.LENGTH_SHORT).show();

                // Handle notification scheduling logic here based on `selectedTime`
                if (selectedTime.equals("A day before")) {
                    notification= "day";
                } else if (selectedTime.equals("An hour before")) {
                    selectedTime.equals("hour");
                }else if(selectedTime.equals("no notification")){
                    selectedTime.equals("no");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when no item is selected (optional)
            }
        });
        ///////
        // Save Task Logic
        saveButton.setOnClickListener(v -> {
            String newTitle = editTitle.getText().toString();
            String newDescription = editDescription.getText().toString();
            String newDueDate = showDueDate.getText().toString();
            String newDueTime = showDueTime.getText().toString();
            int newPriority = spinnerPriority.getSelectedItemPosition() + 1; // Convert to 1-based
            String newCategory = spinnerCategory.getSelectedItem().toString();
            String newStatus = statusCheckBox.isChecked() ? "completed" : "uncompleted";
            String newReminder = notification;

            // Schedule notification
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, selectedYear);
            calendar.set(Calendar.MONTH, selectedMonth);
            calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
            calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
            calendar.set(Calendar.MINUTE, selectedMinute);

            if (!newTitle.isEmpty() && !newDescription.isEmpty() && !newDueDate.isEmpty() && !newDueTime.isEmpty()) {
                if (calendar.getTimeInMillis() >= System.currentTimeMillis()) {
                    // Save task to database
                    DBHelper dbHelper = new DBHelper(this);
                    dbHelper.updateTask(taskId, newTitle, newDueDate, newDueTime, newDescription, newPriority, newCategory, newStatus, newReminder);

                    if (notification.equals("day")) {
                        calendar.add(Calendar.DAY_OF_YEAR, -1);//-1day
                    } else if (notification.equals("hour")) {
                        calendar.add(Calendar.HOUR_OF_DAY, -1);//-1hour
                    }

                    // Ensure the notification time is valid
                    if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                        Toast.makeText(this, "Notification time is in the past!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    scheduleNotification(title, originalDueDate, calendar.getTimeInMillis());

                    ////////
                    // Finish the activity and go back to the previous screen
                    Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent(EditTaskActivity.this, MainActivity.class);
                    startActivity(mainIntent);

                    finish();
                }else{
                    Toast.makeText(this, "Notification time is in the past!", Toast.LENGTH_SHORT).show();
                    return;
                }

            }else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String formatDate(String originalDate) {
        String formattedDate = "";
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("d/M/yyyy");
            Date date = originalFormat.parse(originalDate);

            SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
            formattedDate = targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }
    private void scheduleNotification(String title, String date, long triggerTime) {
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("due_date", date);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
    }

    /////////////////
    /////
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Task Notifications";
            String description = "Channel for task reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("task_channel", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }////

}

