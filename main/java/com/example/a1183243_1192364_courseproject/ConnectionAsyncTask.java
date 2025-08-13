//ConnectionAsyncTask
package com.example.a1183243_1192364_courseproject;

import android.app.Activity;
import android.os.AsyncTask;

import java.util.List;


public class ConnectionAsyncTask extends AsyncTask<String, String, String> {

    private final RestApi fragment;

    public ConnectionAsyncTask(RestApi fragment) {

        this.fragment = fragment;
    }

    @Override
    protected void onPreExecute() {

        if (fragment != null) {
            fragment.setButtonText("Connecting...");
            fragment.setProgress(true);
        }
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        String data = HttpManager.getData(params[0]);
        if (data == null) {
            cancel(true); // Cancel task if data is null
        }
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (fragment != null) {
            fragment.setProgress(false);
            fragment.setButtonText("Connected");

            // Parse JSON and update tasks
            List<Tasks> tasks = TasksJsonParser.getObjectFromJson(s);
            fragment.fillTasks(tasks);
        }
    }
}
