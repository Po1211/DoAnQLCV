package com.example.tahu;

import java.util.Map;
import java.util.List;

public class ProjectsModel {
    private String p_name;
    private String p_description;
    private String p_owner;
    private List<String> p_tasks, p_members;

    public ProjectsModel() {
        // Default constructor required for calls to DataSnapshot.getValue(ProjectsModel.class)
    }

    public ProjectsModel(String p_name, String p_description, String p_owner, List<String> p_members, List<String> p_tasks) {
        this.p_name = p_name;
        this.p_description = p_description;
        this.p_owner = p_owner;
        this.p_members = p_members;
        this.p_tasks = p_tasks;
    }

    // Getters and setters
    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_description() {
        return p_description;
    }

    public void setP_description(String p_description) {
        this.p_description = p_description;
    }

    public String getP_owner() {
        return p_owner;
    }

    public void setP_owner(String p_owner) {
        this.p_owner = p_owner;
    }

    public List<String> getP_members() {
        return p_members;
    }

    public void setP_members(List<String> p_members) {
        this.p_members = p_members;
    }

    public List<String> getP_tasks() {
        return p_tasks;
    }

    public void setP_tasks(List<String> p_tasks) {
        this.p_tasks = p_tasks;
    }
}

