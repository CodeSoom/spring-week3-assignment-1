package com.codesoom.assignment.models;

import java.util.List;

public class Task {
    private Long id;

    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null)
//            return false;
//
//        if (getClass() != obj.getClass())
//            return false;
//
//        List<Task> tasks = (List<Task>)obj;
//        for(Task task : tasks) {
//            if(!(task.getId().equals(this.id) && task.getTitle().equals(this.title)))
//                return false;
//        }
//        return true;
//    }
}
