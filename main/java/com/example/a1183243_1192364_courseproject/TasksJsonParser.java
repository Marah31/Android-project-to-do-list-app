package com.example.a1183243_1192364_courseproject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TasksJsonParser {
    public static List<Tasks> getObjectFromJson(String json) {
        List<Tasks> tasksList;
        try {
            JSONArray jsonArray = new JSONArray(json);
            tasksList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Tasks task = new Tasks();
                task.setTaskId(jsonObject.getLong("taskId"));
                task.setTitle(jsonObject.getString("title"));
                task.setDescription(jsonObject.getString("description"));
                task.setDue_date(jsonObject.getString("due_date"));
                task.setDue_time(jsonObject.getString("due_time"));
                task.setStatus(jsonObject.optString("status", "Pending")); // Default to "Pending" if missing
                task.setPriority(jsonObject.optInt("priority", 3)); // Default priority is 3
                task.setCategory(jsonObject.getString("category"));

                tasksList.add(task);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null; // Return null in case of an error
        }
        return tasksList;
    }
}
