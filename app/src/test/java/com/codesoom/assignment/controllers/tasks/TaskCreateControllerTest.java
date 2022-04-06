package com.codesoom.assignment.controllers.tasks;

import com.codesoom.assignment.domains.Task;
import com.codesoom.assignment.domains.TaskDto;
import com.codesoom.assignment.exceptions.TaskInvalidFormatException;
import com.codesoom.assignment.repositories.InMemoryTaskRepositoryImpl;
import com.codesoom.assignment.services.TaskCreateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TaskCreateControllerTest {

    private TaskCreateController controller;

    private static final String TASK_TITLE ="title";
    private static final String EMPTY_TASK_TITLE = "";
    private static final String BLANK_TASK_TITLE = " ";

    @BeforeEach
    void setup() {
        final TaskCreateServiceImpl service = new TaskCreateServiceImpl(new InMemoryTaskRepositoryImpl());
        this.controller = new TaskCreateController(service);
    }

    @DisplayName("할 일을 성공적으로 추가하면, 추가된 할 일을 반환한다.")
    @Test
    void addTaskTest() {
        final Task task = controller.addTask(new TaskDto("title"));

        assertThat(task).isNotNull();
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @DisplayName("빈 제목의 할 일을 추가하면 예외가 발생한다.")
    @Test
    void thrownTaskInvalidException() {
        assertAll(() -> {
            assertThrows(TaskInvalidFormatException.class,
                    () -> controller.addTask(new TaskDto(EMPTY_TASK_TITLE)));
            assertThrows(TaskInvalidFormatException.class,
                    () -> controller.addTask(new TaskDto(BLANK_TASK_TITLE)));
            assertThrows(TaskInvalidFormatException.class,
                    () -> controller.addTask(new TaskDto(null)));
        });
    }

}
