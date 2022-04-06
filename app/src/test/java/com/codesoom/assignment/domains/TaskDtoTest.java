package com.codesoom.assignment.domains;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class TaskDtoTest {

    private static final String TITLE = "title";

    @DisplayName("생성자로 TaskDto를 성공적으로 생성한다.")
    @Test
    void createTaskDtoAsAllArgsConstructor() {
        final TaskDto taskDto = new TaskDto(TITLE);

        assertThat(taskDto).isNotNull();
        assertThat(taskDto.getTitle()).isEqualTo(TITLE);
    }

    @DisplayName("TaskDto를 테스트 한다.")
    @Test
    void taskDtoTest() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle(TITLE);

        assertThat(taskDto.getTitle()).isEqualTo(TITLE);

        Task task = taskDto.toTask(1L);

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