package com.codesoom.assignment.models;

public class Task {

    private static Long count = 0L;
    private Long id;
    private String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String toString(){
        return "Task - title: " + title;
    }
}
