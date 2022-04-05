package com.codesoom.assignment.domains;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class TaskDtoTest {

    private static final String TITLE = "title";

    @DisplayName("기본 생성자로 TaskDto를 성공적으로 생성한다.")
    @Test
    void createTaskDtoAsNoArgsConstructor() {
        assertThat(new TaskDto()).isNotNull();
    }

    @DisplayName("생성자로 TaskDto를 성공적으로 생성한다.")
    @Test
    void createTaskDtoAsAllArgsConstructor() {
        final TaskDto taskDto = new TaskDto(TITLE);

        assertThat(taskDto).isNotNull();
        assertThat(taskDto.getTitle()).isEqualTo(TITLE);
    }

    @DisplayName("setTitle()은 title에 값을 세팅한다.")
    @Test
    void setTitle() {
        final TaskDto taskDto = new TaskDto();
        taskDto.setTitle(TITLE);

        assertThat(taskDto.getTitle()).isEqualTo(TITLE);
    }

    @DisplayName("getTitle()은 title을 반환한다.")
    @Test
    void getTitle() {
        final TaskDto taskDto = new TaskDto(TITLE);

        assertThat(taskDto.getTitle()).isEqualTo(TITLE);
    }

    @DisplayName("toTask()는 Task 객체로 만들어 반환한다.")
    @Test
    void toTask() {
        final TaskDto taskDto = new TaskDto(TITLE);

        final Task task = taskDto.toTask(1L);
        assertThat(task).isInstanceOf(Task.class);
        assertThat(task.getTitle()).isEqualTo(TITLE);
    }

    @DisplayName("toString()은 TaskDto의 정보를 문자열로 만들어 반환한다.")
    @Test
    void toStringTest() {
        final TaskDto taskDto = new TaskDto(TITLE);

        assertThat(taskDto.toString()).isInstanceOf(String.class);
        assertThat(taskDto.toString()).contains(TITLE);
    }

}