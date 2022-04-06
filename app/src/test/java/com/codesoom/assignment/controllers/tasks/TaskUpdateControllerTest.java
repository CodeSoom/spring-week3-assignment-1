package com.codesoom.assignment.controllers.tasks;

import com.codesoom.assignment.domains.Task;
import com.codesoom.assignment.domains.TaskDto;
import com.codesoom.assignment.exceptions.TaskInvalidFormatException;
import com.codesoom.assignment.exceptions.TaskNotFoundException;
import com.codesoom.assignment.repositories.InMemoryTaskRepositoryImpl;
import com.codesoom.assignment.services.TaskUpdateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class TaskUpdateControllerTest {

    private TaskUpdateController controller;

    private static final Long EXIST_ID = 1L;
    private static final Task SAVED_TASK = new Task(EXIST_ID, "title");
    private static final Long NOT_EXIST_ID = 100L;

    private static final String EMPTY_TASK_TITLE = "";
    private static final String BLANK_TASK_TITLE = " ";

    @BeforeEach
    void setup() {
        final InMemoryTaskRepositoryImpl repository = new InMemoryTaskRepositoryImpl();
        repository.save(SAVED_TASK);

        final TaskUpdateServiceImpl service = new TaskUpdateServiceImpl(repository);
        this.controller = new TaskUpdateController(service);
    }

    @DisplayName("할 일을 성공적으로 변경한다.")
    @Test
    void updateTask() {
        final Task task = controller.updateTask(EXIST_ID, new TaskDto("new title"));

        assertThat(task).isNotNull();
        assertThat(task.getTitle()).isEqualTo("new title");
    }

    @DisplayName("존재하지 않는 할 일의 수정 요청이 오면 예외가 발생한다.")
    @Test
    void thrownNotFoundException() {
        assertThrows(TaskNotFoundException.class,
                () -> controller.updateTask(NOT_EXIST_ID, new TaskDto("new title")));
    }

    @DisplayName("빈 제목으로 할 일을 수정하려고 하면 예외가 발생한다.")
    @Test
    void thrownInvalidFormatException() {
        assertAll(() -> {
            assertThrows(TaskInvalidFormatException.class,
                    () -> controller.updateTask(NOT_EXIST_ID, new TaskDto(EMPTY_TASK_TITLE)));
            assertThrows(TaskInvalidFormatException.class,
                    () -> controller.updateTask(NOT_EXIST_ID, new TaskDto(BLANK_TASK_TITLE)));
            assertThrows(TaskInvalidFormatException.class,
                    () -> controller.updateTask(NOT_EXIST_ID, new TaskDto(null)));
        });
    }

}
