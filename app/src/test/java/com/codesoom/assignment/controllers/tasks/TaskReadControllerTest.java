package com.codesoom.assignment.controllers.tasks;

import com.codesoom.assignment.domains.Task;
import com.codesoom.assignment.exceptions.TaskNotFoundException;
import com.codesoom.assignment.repositories.InMemoryTaskRepositoryImpl;
import com.codesoom.assignment.services.TaskReadServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskReadControllerTest {

    private TaskReadController controller;

    private static final Long EXIST_ID = 1L;
    private static final Task SAVED_TASK = new Task(EXIST_ID, "title");
    private static final Long NOT_EXIST_ID = 100L;

    @BeforeEach
    void setup() {
        final InMemoryTaskRepositoryImpl repository = new InMemoryTaskRepositoryImpl();
        repository.save(SAVED_TASK);

        final TaskReadServiceImpl service = new TaskReadServiceImpl(repository);
        this.controller = new TaskReadController(service);
    }

    @DisplayName("모든 할 일을 성공적으로 반환한다.")
    @Test
    void getTasks() {
        assertThat(controller.getTasks()).isNotEmpty();
    }

    @DisplayName("id와 일치하는 할 일이 있으면 해당 할 일을 반환하고, 없으면 예외가 발생한다.")
    @Test
    void getTask() {
        assertThat(controller.getTask(SAVED_TASK.getId())).isNotNull();
        assertThrows(TaskNotFoundException.class, () -> controller.getTask(NOT_EXIST_ID));
    }

}
