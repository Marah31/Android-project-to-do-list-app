//RestApi
package com.example.a1183243_1192364_courseproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class RestApi extends Fragment {
    Button importButton;
    LinearLayout linearLayout;
    ProgressBar progressBar;

    public RestApi() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rest_api, container, false);

        importButton = rootView.findViewById(R.id.button);
        linearLayout = rootView.findViewById(R.id.layout);
        progressBar = rootView.findViewById(R.id.progressBar);

        setProgress(false);

        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionAsyncTask connectionAsyncTask = new
                        ConnectionAsyncTask(RestApi.this);
                connectionAsyncTask.execute("https://mocki.io/v1/d869dfa5-5498-4891-8ac2-dd42aa1f93a2");
            }
        });

        return rootView;
    }

    public void setButtonText(String text) {
        importButton.setText(text);
    }

    public void fillTasks(List<Tasks> tasks) {
        linearLayout.removeAllViews();

        if (tasks == null || tasks.isEmpty()) {
            TextView noTasksView = new TextView(getContext());
            noTasksView.setText("No tasks available.");
            noTasksView.setPadding(16, 16, 16, 16);
            linearLayout.addView(noTasksView);
            return;
        }

        for (Tasks task : tasks) {
            View taskView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, linearLayout, false);

            TextView taskTitle = taskView.findViewById(R.id.taskTitle);
            TextView taskDescription = taskView.findViewById(R.id.taskDescription);

            taskTitle.setText(task.getTitle()); // Assuming Tasks class has getTitle()
            taskDescription.setText(task.getDescription()); // Assuming Tasks class has getDescription()

            linearLayout.addView(taskView);
        }
    }

    public void setProgress(boolean progress) {
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}

/*public class RestApi extends Fragment {
    Button importButton;
    LinearLayout linearLayout;
    ProgressBar progressBar;


    public RestApi() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rest_api, container, false);


        importButton = rootView.findViewById(R.id.button);
        linearLayout = rootView.findViewById(R.id.layout);
        progressBar = rootView.findViewById(R.id.progressBar);
        setProgress(false);

        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionAsyncTask connectionAsyncTask = new
                        ConnectionAsyncTask(RestApi.this);
                connectionAsyncTask.execute("https://mocki.io/v1/d869dfa5-5498-4891-8ac2-dd42aa1f93a2");
            }
        });
        return rootView;
    }
    public void setButtonText(String text) {

        importButton.setText(text);
    }

    public void fillTasks(List<Tasks> tasks) {
        linearLayout.removeAllViews();

        if (tasks == null || tasks.isEmpty()) {
            TextView noTasksView = new TextView(getContext());
            noTasksView.setText("No tasks available.");
            linearLayout.addView(noTasksView);
            return;
        }

        for (Tasks task : tasks) {
            TextView taskView = new TextView(getContext());
            taskView.setText(task.toString());
            linearLayout.addView(taskView);
        }
    }
    public void setProgress(boolean progress) {
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

}*/
