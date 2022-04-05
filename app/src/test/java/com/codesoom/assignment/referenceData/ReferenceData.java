package com.codesoom.assignment.referenceData;

public enum ReferenceData {
    FIRST_TODO("첫번째 할일"),
    SECOND_TODO("두번째 할일"),
    UPDATE_TODO("수정될 할일");

    private final String title;
    ReferenceData(String title){
        this.title=title;
    }

    public String getTitle() {
        return title;
    }
}
