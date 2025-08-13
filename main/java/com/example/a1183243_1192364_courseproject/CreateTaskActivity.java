//CreateTaskActivity
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
import android.util.Log;
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

import java.util.Calendar;
import java.util.Locale;

public class CreateTaskActivity extends AppCompatActivity {

    private EditText editTitle, editDescription;
    private TextView textDueDate, textDueTime;
    private Button buttonSelectDate, buttonSelectTime, buttonSave;
    private Spinner spinnerPriority, spinnerCategory;
    private CheckBox stateCheckBox;
    ///
    private Spinner spinnerNotificationTimes;
    ////
    private String dueDate = "", dueTime = "", notification= "";
    ////
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;
    ///////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        createNotificationChannel();
        // Initialize Views
        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        textDueDate = findViewById(R.id.textDueDate);
        buttonSelectDate = findViewById(R.id.buttonSelectDate);
        textDueTime = findViewById(R.id.textDueTime);
        buttonSelectTime = findViewById(R.id.buttonSelectTime);
        buttonSave = findViewById(R.id.buttonSave);
        spinnerPriority = findViewById(R.id.prioritySpinner);
        spinnerCategory = findViewById(R.id.categorySpinner);
        stateCheckBox = findViewById(R.id.state_check);
        ////
        spinnerNotificationTimes = findViewById(R.id.spinner_notification_times);
        /////
        // Set up Priority Spinner
        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(this,
                R.array.priority_array, android.R.layout.simple_spinner_item);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(priorityAdapter);

        // Set up Category Spinner
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // Date Picker Logic
        buttonSelectDate.setOnClickListener(v -> {
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

                    dueDate = daySelected + "/" + (monthSelected + 1) + "/" + yearSelected;
                    textDueDate.setText(dueDate);
                }
            }, year, month, day);
            datePickerDialog.show();
        });


        // Time Picker Logic
        buttonSelectTime.setOnClickListener(v -> {
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

                    dueTime = String.format(Locale.getDefault(), "%02d:%02d", hourSelected, minuteSelected);
                    textDueTime.setText(dueTime);

            }, hour, minute, true);
            timePickerDialog.show();
        });

        ///////////////////////////
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
                Toast.makeText(CreateTaskActivity.this, "Selected: " + selectedTime, Toast.LENGTH_SHORT).show();

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
        ////////////////////////////////
        // Save Task Logic
        buttonSave.setOnClickListener(v -> {
            String title = editTitle.getText().toString();
            String description = editDescription.getText().toString();
            // Schedule notification
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, selectedYear);
            calendar.set(Calendar.MONTH, selectedMonth);
            calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
            calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
            calendar.set(Calendar.MINUTE, selectedMinute);

            if (!title.isEmpty() && !description.isEmpty() && !dueDate.isEmpty() && !dueTime.isEmpty()) {
                if (calendar.getTimeInMillis() >= System.currentTimeMillis()) {
                int priority = getPriorityFromSpinner();
                String category = spinnerCategory.getSelectedItem().toString();
                String status = stateCheckBox.isChecked() ? "completed" : "uncompleted";
                String reminder = notification;
                // Save task to database
                DBHelper dbHelper = new DBHelper(this);
                dbHelper.addTask(title, description, dueDate, dueTime, priority, status, category, reminder);
                ////////


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

                scheduleNotification(title, dueDate, calendar.getTimeInMillis());

                ////////
                // Finish the activity and go back to the previous screen
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

    private int getPriorityFromSpinner() {
        switch (spinnerPriority.getSelectedItemPosition()) {
            case 0:
                return 1; // High
            case 1:
                return 2; // Medium
            case 2:
                return 3; // Low
            default:
                return 3; // Default to Low if not selected
        }
    }
    /////////////
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
