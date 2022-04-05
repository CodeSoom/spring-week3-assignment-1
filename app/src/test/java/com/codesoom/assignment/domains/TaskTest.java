package com.codesoom.assignment.domains;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


class TaskTest {

    @DisplayName("기본 생성자로 Task 인스턴스를 생성한다.")
    @Test
    void createTaskAsNoArgsConstructor() {
        assertThat(new Task()).isNotNull();
    }

    @DisplayName("생성자로 Task를 성공적으로 생성한다.")
    @Test
    void createTaskAsAllArgsConstructor() {
        //given
        final Long id = 1L;
        final String title = "title";

        //when
        final Task task = new Task(id, title);

        //then
        assertAll(() -> {
            assertThat(task).isNotNull();
            assertThat(task.getId()).isEqualTo(id);
            assertThat(task.getTitle()).isEqualTo(title);
        });
    }

    @DisplayName("getId()는 id를 성공적으로 반환한다.")
    @Test
    void getId() {
        //given
        final Long id = 1L;
        final Task task = new Task(id, "title");

        //when
        final Long _id = task.getId();

        //then
        assertThat(_id).isEqualTo(id);
    }

    @DisplayName("getTitle()은 title을 성공적으로 반환한다.")
    @Test
    void getTitle() {
        //given
        final String title = "title";
        final Task task = new Task(1L, title);

        //when
        final String _title = task.getTitle();

        //then
        assertThat(_title).isEqualTo(title);
    }

    @DisplayName("Task의 title을 성공적으로 변경한다.")
    @Test
    void updateTitle() {
        //given
        final Task task = new Task(1L, "title");
        String newTitle = "new title";

        //when
        task.updateTitle(newTitle);

        //then
        assertThat(task.getTitle()).isEqualTo(newTitle);
    }

}