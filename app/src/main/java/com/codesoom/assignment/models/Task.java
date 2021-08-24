package com.codesoom.assignment.models;

/**
 * 할 일 정보를 저장하고 처리합니다.
 */
public class Task {
    private final Long id;

    private final String title;

    public Task(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    /**
     * 할 일의 식별자를 리턴합니다.
     * @return 할 일의 식별자
     */
    public Long getId() {
        return id;
    }

    /**
     * 할 일의 제목을 리턴합니다.
     * @return 할 일의 제목
     */
    public String getTitle() {
        return title;
    }
}
