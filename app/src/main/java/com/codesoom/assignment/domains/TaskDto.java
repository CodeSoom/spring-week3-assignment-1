package com.codesoom.assignment.domains;

public class TaskDto {

    private String title;

    public TaskDto() {
    }

    public TaskDto(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Task toTask() {
        return new Task(title);
    }

    @Override
    public String toString() {
        return "TaskDto{" +
                "title='" + title + '\'' +
                '}';
    }

}

