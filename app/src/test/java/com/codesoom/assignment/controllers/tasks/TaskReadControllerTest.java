package com.codesoom.assignment.controllers.tasks;

import com.codesoom.assignment.domains.Task;
import com.codesoom.assignment.exceptions.TaskNotFoundException;
import com.codesoom.assignment.repositories.InMemoryTaskRepository;
import com.codesoom.assignment.services.TaskReadService;
import com.codesoom.assignment.services.TaskReadServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskReadControllerTest {

    private TaskReadController controller;

    private Long EXIST_ID;
    private static final Long NOT_EXIST_ID = 100L;

    @BeforeEach
    void setup() {
        final InMemoryTaskRepository repository = new InMemoryTaskRepository();
        Task task = new Task("title");
        this.EXIST_ID = repository.save(task).getId();

        final TaskReadService service = new TaskReadServiceImpl(repository);
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
        assertThat(controller.getTask(EXIST_ID)).isNotNull();
        assertThrows(TaskNotFoundException.class, () -> controller.getTask(NOT_EXIST_ID));
    }

}
