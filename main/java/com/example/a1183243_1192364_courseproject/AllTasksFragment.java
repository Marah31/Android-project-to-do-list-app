package com.example.a1183243_1192364_courseproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllTasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllTasksFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListView listTasks;
    private DBHelper dbHelper;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllTasksFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Tasks> taskList;

    public static AllTasksFragment newInstance(String param1, String param2) {
        AllTasksFragment fragment = new AllTasksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment's layout
        View rootView = inflater.inflate(R.layout.fragment_all_tasks, container, false);

        // Initialize ListView and DBHelper
        listTasks = rootView.findViewById(R.id.listTasks1); // Correctly find ListView
        dbHelper = new DBHelper(getContext()); // Use getContext() instead of 'this'

        // Load tasks from the database
        loadTasks();
        Log.d("AllTasksFragment", "listTasks is " + (listTasks == null ? "null" : "not null"));

        return rootView;
    }

    private void loadTasks() {
        // Get all tasks from the database
        Cursor cursor = dbHelper.getAllTasks();

        // Debugging: Check if there are any tasks in the database
        if (cursor == null || cursor.getCount() == 0) {
            Log.d("AllTasksFragment", "No tasks found in the database.");
        } else {
            Log.d("AllTasksFragment", "Found " + cursor.getCount() + " tasks.");
        }

        // Define the columns to show and where to show them in the layout
        String[] from = {DBHelper.COLUMN_TITLE, DBHelper.COLUMN_DUE_DATE}; // The columns to bind
        int[] to = {android.R.id.text1, android.R.id.text2}; // Where to display the columns in the layout

        // Create a SimpleCursorAdapter to display the data in the ListView
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getContext(), // Pass the context
                android.R.layout.simple_list_item_2, // Layout for each list item
                cursor, // Cursor containing the data
                from, // The columns from the cursor to bind
                to, // The layout views to bind the data to
                0 // Flags (0 is usually fine)
        );

        // Set the adapter to the ListView
        listTasks.setAdapter(adapter);

        // Log the adapter setup
        Log.d("AllTasksFragment", "Adapter initialized with " + cursor.getCount() + " items.");

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