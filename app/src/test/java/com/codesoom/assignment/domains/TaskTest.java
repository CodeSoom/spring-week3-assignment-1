package com.codesoom.assignment.domains;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


class TaskTest {

    private static final Long ID = 1L;
    private static final String TITLE = "title";
    private static final String NEW_TITLE = "new title";

    @DisplayName("기본 생성자로 Task 인스턴스를 생성한다.")
    @Test
    void createTaskAsNoArgsConstructor() {
        assertThat(new Task()).isNotNull();
    }

    @DisplayName("생성자로 Task를 성공적으로 생성한다.")
    @Test
    void createTaskAsAllArgsConstructor() {
        final Task task = new Task(ID, TITLE);

        assertAll(() -> {
            assertThat(task).isNotNull();
            assertThat(task.getId()).isEqualTo(ID);
            assertThat(task.getTitle()).isEqualTo(TITLE);
        });
    }

    @DisplayName("getId()는 id를 성공적으로 반환한다.")
    @Test
    void getId() {
        final Task task = new Task(ID, TITLE);

        assertThat(task.getId()).isEqualTo(ID);
    }

    @DisplayName("getTitle()은 title을 성공적으로 반환한다.")
    @Test
    void getTitle() {
        final Task task = new Task(ID, TITLE);

        assertThat(task.getTitle()).isEqualTo(TITLE);
    }

    @DisplayName("Task의 title을 성공적으로 변경한다.")
    @Test
    void updateTitle() {
        final Task task = new Task(ID, TITLE);
        task.updateTitle(NEW_TITLE);

        assertThat(task.getTitle()).isEqualTo(NEW_TITLE);
    }


    @DisplayName("toString()은 Task의 정보를 문자열로 만들어 반환한다.")
    @Test
    void toStringTest() {
        final Task task = new Task(ID, TITLE);

        assertAll(() -> {
            assertThat(task.toString()).isInstanceOf(String.class);
            assertThat(task.toString()).contains(TITLE);
            assertThat(task.toString()).contains(ID.toString());
        });
    }

}