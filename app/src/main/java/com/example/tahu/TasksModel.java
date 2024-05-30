package com.example.tahu;

public class TasksModel {
    private String t_title;
    private String t_description;
    private String t_assigned_to;
    private String t_status;

    public TasksModel() {
        // Default constructor required for calls to DataSnapshot.getValue(TasksModel.class)
    }

    public TasksModel(String t_title, String t_description, String t_assigned_to, String t_status) {
        this.t_title = t_title;
        this.t_description = t_description;
        this.t_assigned_to = t_assigned_to;
        this.t_status = t_status;
    }

    // Getters and setters
    public String getT_title() {
        return t_title;
    }

    public void setT_title(String t_title) {
        this.t_title = t_title;
    }

    public String getT_description() {
        return t_description;
    }

    public void setT_description(String t_description) {
        this.t_description = t_description;
    }

    public String getT_assigned_to() {
        return t_assigned_to;
    }

    public void setT_assigned_to(String t_assigned_to) {
        this.t_assigned_to = t_assigned_to;
    }

    public String getT_status() {
        return t_status;
    }

    public void setT_status(String t_status) {
        this.t_status = t_status;
    }
}

