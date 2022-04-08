package com.codesoom.assignment.controllers.tasks;

import com.codesoom.assignment.domains.Task;
import com.codesoom.assignment.domains.TaskDto;
import com.codesoom.assignment.exceptions.TaskInvalidFormatException;
import com.codesoom.assignment.exceptions.TaskNotFoundException;
import com.codesoom.assignment.repositories.InMemoryTaskRepository;
import com.codesoom.assignment.services.TaskUpdateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class TaskUpdateControllerTest {

    private TaskUpdateController controller;

    private Long EXIST_ID;
    private static final Long NOT_EXIST_ID = 100L;

    @BeforeEach
    void setup() {
        final InMemoryTaskRepository repository = new InMemoryTaskRepository();
        Task task = new Task("title");
        this.EXIST_ID = repository.save(task).getId();

        final TaskUpdateServiceImpl service = new TaskUpdateServiceImpl(repository);
        this.controller = new TaskUpdateController(service);
    }

    @DisplayName("할 일을 성공적으로 변경한다.")
    @Test
    void updateTask() {
        final String newTitle = "new title";
        final Task task = controller.updateTask(EXIST_ID, new TaskDto(newTitle));

        assertThat(task).isNotNull();
        assertThat(task.getTitle()).isEqualTo(newTitle);
    }

    @DisplayName("존재하지 않는 할 일의 수정 요청이 오면 예외가 발생한다.")
    @Test
    void thrownNotFoundException() {
        assertThrows(TaskNotFoundException.class,
                () -> controller.updateTask(NOT_EXIST_ID, new TaskDto("new title")));
    }

    @DisplayName("으로 할 일을 수정할 경우 예외가 발생한다.")
    @NullSource
    @ValueSource(strings = {"", " "})
    @ParameterizedTest(name = "[{index}] - \"{0}\"{displayName}")
    void thrownInvalidFormatException(String requestTitle) {
        assertThrows(TaskInvalidFormatException.class,
                () -> controller.updateTask(NOT_EXIST_ID, new TaskDto(requestTitle)));
    }

}
