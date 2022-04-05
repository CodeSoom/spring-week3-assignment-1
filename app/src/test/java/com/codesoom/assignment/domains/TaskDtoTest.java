package com.codesoom.assignment.domains;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class TaskDtoTest {

    @DisplayName("기본 생성자로 TaskDto를 성공적으로 생성한다.")
    @Test
    void createTaskDtoAsNoArgsConstructor() {
        assertThat(new TaskDto()).isNotNull();
    }

    @DisplayName("생성자로 TaskDto를 성공적으로 생성한다.")
    @Test
    void createTaskDtoAsAllArgsConstructor() {
        final String title = "title";

        final TaskDto taskDto = new TaskDto(title);

        assertThat(taskDto).isNotNull();
        assertThat(taskDto.getTitle()).isEqualTo(title);
    }

    @DisplayName("setTitle()은 title에 값을 세팅한다.")
    @Test
    void setTitle() {
        final TaskDto taskDto = new TaskDto();

        final String title = "title";
        taskDto.setTitle(title);

        assertThat(taskDto.getTitle()).isEqualTo(title);
    }

    @DisplayName("getTitle()은 title을 반환한다.")
    @Test
    void getTitle() {
        final String title = "title";
        final TaskDto taskDto = new TaskDto(title);

        final String _title = taskDto.getTitle();

        assertThat(_title).isEqualTo(title);
    }

    @DisplayName("toTask()는 Task 객체로 만들어 반환한다.")
    @Test
    void toTask() {
        final String title = "title";
        final TaskDto taskDto = new TaskDto(title);

        assertThat(taskDto.toTask(1L)).isInstanceOf(Task.class);
        assertThat(taskDto.toTask(1L).getTitle()).isEqualTo(title);
    }

}