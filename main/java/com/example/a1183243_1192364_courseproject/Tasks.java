package com.example.a1183243_1192364_courseproject;

public class Tasks {
    private long taskId ;
    private String title;
    private String description;
    private String due_date;
    private String due_time;
    private String status;
    private int priority;
    private String category;
    private String reminder;
    public Tasks() {

    }
    public Tasks(long taskId, String title, String description, String due_date,String due_time, String status, int priority, String category, String reminder) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.due_date = due_date;
        this.due_time= due_time;
        this.status = (status != null) ? status : "Pending";
        this.priority = (priority == 0) ? 3 : priority;
        this.category = category;
        this.reminder = reminder;

    }
    public Tasks(String title, String description, String due_date,String due_time,  String status, int priority, String category, String reminder) {
        this.title = title;
        this.description = description;
        this.due_date = due_date;
        this.due_time= due_time;
        this.status = (status != null) ? status : "Pending";
        this.priority = priority;
        this.category = category;
        this.reminder = reminder;
    }


    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getDue_time() {
        return due_time;
    }

    public void setDue_time(String due_time) {
        this.due_time = due_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    @Override
    public String toString() {
        return "Task{" +
                "\n, task title='" + title + '\'' +
                "\n, description='" + description + '\'' +
                "\n, due date='" + due_date + '\'' +
                "\n, due time='" + due_time + '\'' +
                "\n,priority='" + priority + '\'' +
                "\n, statue='" + status + '\'' +
                "\n, category='" + category + '\'' +
                "\n, reminder='" + reminder + '\'' +
                "\n}\n\n";
    }

}
