package com.example.a1183243_1192364_courseproject;

import android.content.Intent;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TodayTasksFragment extends Fragment {

    private ListView listTasks;
    private DBHelper dbHelper;
    private static final String DATE_FORMAT = "dd/MM/yyyy";  // Format of the date in the database
    private SimpleDateFormat dateFormat;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_today_tasks, container, false);
        listTasks = rootView.findViewById(R.id.listTasks);
        dbHelper = new DBHelper(getActivity());

        dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        String todayDate = dateFormat.format(new Date());  // Get today's date in the same format as in DB

        loadTodayTasks(todayDate);  // Load tasks that are due today

        return rootView;
    }

    private void loadTodayTasks(String todayDate) {
        Cursor cursor = dbHelper.getTasksByDueDate(todayDate); // Get tasks with today's due date

        if (cursor != null && cursor.getCount() > 0) {
            // Map the columns from the database to views in the layout
            String[] fromColumns = {DBHelper.COLUMN_TITLE, DBHelper.COLUMN_DUE_DATE};
            int[] toViews = {android.R.id.text1, android.R.id.text2};

            // Create a SimpleCursorAdapter
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    getContext(),
                    android.R.layout.simple_list_item_2, // Use a layout that has two TextViews
                    cursor,
                    fromColumns,
                    toViews,
                    0
            );

            // Set the adapter to the ListView
            listTasks.setAdapter(adapter);

            // Handle item clicks
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

}

