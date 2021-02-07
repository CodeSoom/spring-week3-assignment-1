package com.codesoom.assignment.models;

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

    /**
     * Task 가 가진 값들이 같은지 비교합니다.
     *
     * @param obj 비교할 대상.
     * @return {@code true} 모든 값이 같을 때;
     * {@code false} 아닐 경우.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Task) {
            Task task = (Task) obj;
            return id.equals(task.getId()) && title.equals(task.getTitle());
        }
        return false;
    }
}
