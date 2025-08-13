package com.example.a1183243_1192364_courseproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class SearchResultsActivity extends AppCompatActivity {
    private ListView listTasks;
    private DBHelper dbHelper;
    private String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_tasks);

        listTasks = findViewById(R.id.listfilteredtasks);
        dbHelper = new DBHelper(this);

        // Get the search query from the Intent
        Intent intent = getIntent();
        query = intent.getStringExtra("query");  // Get the search query

        loadTasks(query);  // Pass the query to the loadTasks method
    }

    private void loadTasks(String query) {
        Cursor cursor = dbHelper.getTasksByKeyword(query);  // Use the query to filter tasks
        String[] from = {DBHelper.COLUMN_TITLE, DBHelper.COLUMN_DUE_DATE};
        int[] to = {android.R.id.text1, android.R.id.text2};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, cursor, from, to, 0);
        listTasks.setAdapter(adapter);

        listTasks.setOnItemClickListener((parent, view, position, id) -> {
            Cursor clickedTask = (Cursor) adapter.getItem(position);
            String taskId = clickedTask.getString(clickedTask.getColumnIndex(DBHelper.COLUMN_ID));
            String title = clickedTask.getString(clickedTask.getColumnIndex(DBHelper.COLUMN_TITLE));
            String description = clickedTask.getString(clickedTask.getColumnIndex(DBHelper.COLUMN_DESCRIPTION));
            String dueDate = clickedTask.getString(clickedTask.getColumnIndex(DBHelper.COLUMN_DUE_DATE));
            int priority = clickedTask.getInt(clickedTask.getColumnIndex(DBHelper.COLUMN_PRIORITY));
            String category = clickedTask.getString(clickedTask.getColumnIndex(DBHelper.COLUMN_CATEGORY));
            String status = clickedTask.getString(clickedTask.getColumnIndex(DBHelper.COLUMN_STATUS));

            Intent detailIntent = new Intent(SearchResultsActivity.this, TaskDetailActivity.class);
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

