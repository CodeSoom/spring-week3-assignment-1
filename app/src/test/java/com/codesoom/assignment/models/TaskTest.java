package com.codesoom.assignment.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Task 클래스")
class TaskTest {
    final long givenID = 1;
    final String givenTitle = "sample";

    final long modifiedID = 2;
    final String modifiedTitle = "modified sample";

    Task subject() {
        final Task task = new Task();

        task.setId(givenID);
        task.setTitle(givenTitle);

        return task;
    }

    @Test
    @DisplayName("getId 메셔드는 id 를 리턴한다.")
    void getId() {
        Task task = subject();

        assertThat(task.getId()).isEqualTo(givenID);
    }

    @Test
    @DisplayName("setId 메서드로 id를 입력한 뒤, getId 메서드로 확인하면 setId 로 입력한 id를 리턴한다.")
    void setId() {
        Task task = subject();
        task.setId(modifiedID);

        assertThat(task.getId()).isEqualTo(modifiedID);
    }

    @Test
    @DisplayName("getTitle 메서드는 title 을 리턴한다.")
    void getTitle() {
        Task task = subject();

        assertThat(task.getTitle()).isEqualTo(givenTitle);
    }

    @Test
    @DisplayName("setTitle 메서드로 title 을 입력한 뒤, getTitle 메서드로 확인하면 setTitle 로 입력한 title 을 리턴한다.")
    void setTitle() {
        Task task = subject();
        task.setTitle(modifiedTitle);

        assertThat(task.getTitle()).isEqualTo(modifiedTitle);
    }
}
