package com.codesoom.assignment.controllers.tasks;

import com.codesoom.assignment.domains.Task;
import com.codesoom.assignment.domains.TaskDto;
import com.codesoom.assignment.exceptions.TaskInvalidFormatException;
import com.codesoom.assignment.repositories.InMemoryTaskRepository;
import com.codesoom.assignment.services.TaskCreateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;


public class TaskCreateControllerTest {

    private TaskCreateController controller;

    private static final String TASK_TITLE ="title";

    @BeforeEach
    void setup() {
        final TaskCreateServiceImpl service = new TaskCreateServiceImpl(new InMemoryTaskRepository());
        this.controller = new TaskCreateController(service);
    }

    @DisplayName("할 일을 성공적으로 추가하면, 추가된 할 일을 반환한다.")
    @Test
    void addTaskTest() {
        final Task task = controller.addTask(new TaskDto("title"));

        assertThat(task).isNotNull();
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @DisplayName("으로 할 일을 추가 할 경우 예외가 발생한다.")
    @NullSource
    @ValueSource(strings = {"", " "})
    @ParameterizedTest(name = "[{index}] - \"{0}\"{displayName}")
    void thrownTaskInvalidException(String title) {
        assertThrows(TaskInvalidFormatException.class,
                () -> controller.addTask(new TaskDto(title)));
    }

}
