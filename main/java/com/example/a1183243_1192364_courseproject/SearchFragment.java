package com.example.a1183243_1192364_courseproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SearchFragment extends Fragment {

    private EditText startDateEditText;
    private EditText endDateEditText;
    private Button searchButton;
    private DBHelper dbHelper;
    private ListView listTasks; // Add ListView reference for displaying tasks
    private SimpleCursorAdapter adapter;
    private String startDate = "";
    private String endDate = "";

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(getContext()); // Initialize your DBHelper
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        startDateEditText = rootView.findViewById(R.id.startDateEditText);
        endDateEditText = rootView.findViewById(R.id.endDateEditText);
        searchButton = rootView.findViewById(R.id.searchButton);
        listTasks = rootView.findViewById(R.id.searchedlistTasks);
        dbHelper = new DBHelper(getContext());

        // Set Date Picker for Start Date
        startDateEditText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
                startDate = formatDateToDatabaseFormat(year, month, dayOfMonth);
                startDateEditText.setText(startDate);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        // Set Date Picker for End Date
        endDateEditText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
                endDate = formatDateToDatabaseFormat(year, month, dayOfMonth);
                endDateEditText.setText(endDate);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        // Search Button Logic
        searchButton.setOnClickListener(v -> {
            if (startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(getContext(), "Please select both start and end dates", Toast.LENGTH_SHORT).show();
                return;
            }

            // Query tasks within the date range
            Cursor cursor = dbHelper.getTasksByDateRange(startDate, endDate);
            if (cursor != null && cursor.moveToFirst()) {
                // Handle displaying tasks in the ListView
                displayTasks(cursor);
            } else {
                Toast.makeText(getContext(), "No tasks found for the selected date range", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    // Format the date to match the database format (dd/MM/yyyy)
    private String formatDateToDatabaseFormat(int year, int month, int dayOfMonth) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        Date date = calendar.getTime();
        return dateFormat.format(date);
    }

    // Method to display the tasks in the ListView
    private void displayTasks(Cursor cursor) {
        // Define the columns to show and where to show them in the layout
        String[] from = {DBHelper.COLUMN_TITLE, DBHelper.COLUMN_DUE_DATE}; // The columns to bind
        int[] to = {android.R.id.text1, android.R.id.text2}; // Where to display the columns in the layout

        // Create a SimpleCursorAdapter to display the data in the ListView
        adapter = new SimpleCursorAdapter(
                getContext(), // Pass the context
                android.R.layout.simple_list_item_2, // Layout for each list item
                cursor, // Cursor containing the data
                from, // The columns from the cursor to bind
                to, // The layout views to bind the data to
                0 // Flags (0 is usually fine)
        );

        // Set the adapter to the ListView
        listTasks.setAdapter(adapter);

        // Handle item clicks to view task details
        listTasks.setOnItemClickListener((parent, view, position, id) -> {
            Cursor clickedTask = (Cursor) adapter.getItem(position);

            // Get the task details from the cursor
            String taskId = clickedTask.getString(clickedTask.getColumnIndex(DBHelper.COLUMN_ID));
            String title = clickedTask.getString(clickedTask.getColumnIndex(DBHelper.COLUMN_TITLE));
            String description = clickedTask.getString(clickedTask.getColumnIndex(DBHelper.COLUMN_DESCRIPTION));
            String dueDate = clickedTask.getString(clickedTask.getColumnIndex(DBHelper.COLUMN_DUE_DATE));
            int priority = clickedTask.getInt(clickedTask.getColumnIndex(DBHelper.COLUMN_PRIORITY));
            String category = clickedTask.getString(clickedTask.getColumnIndex(DBHelper.COLUMN_CATEGORY));
            String status = clickedTask.getString(clickedTask.getColumnIndex(DBHelper.COLUMN_STATUS));

            // Create an Intent to open the TaskDetailActivity with the selected task's details
            Intent detailIntent = new Intent(getActivity(), TaskDetailActivity.class);
            detailIntent.putExtra("taskId", taskId);
            detailIntent.putExtra("title", title);
            detailIntent.putExtra("description", description);
            detailIntent.putExtra("dueDate", dueDate);
            detailIntent.putExtra("priority", priority);
            detailIntent.putExtra("category", category);
            detailIntent.putExtra("status", status);

            startActivity(detailIntent);
        });
    }
}
