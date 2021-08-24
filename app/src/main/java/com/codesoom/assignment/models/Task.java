package com.codesoom.assignment.models;

/**
 * 할 일 정보를 저장하고 처리합니다.
 */
public class Task {
    private Long id;

    private String title;

    /**
     * 할 일의 식별자를 리턴합니다.
     * @return 할 일의 식별자
     */
    public Long getId() {
        return id;
    }

    /**
     * 할 일의 식별자를 설정합니다.
     * @param id 할 일의 식별자
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 할 일의 제목을 리턴합니다.
     * @return 할 일의 제목
     */
    public String getTitle() {
        return title;
    }

    /**
     * 할 일의 제목을 설정합니다.
     * @param title 할 일의 제목
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
