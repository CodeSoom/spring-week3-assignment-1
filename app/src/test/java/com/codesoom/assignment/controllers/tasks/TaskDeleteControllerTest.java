package com.codesoom.assignment.controllers.tasks;

import com.codesoom.assignment.domains.Task;
import com.codesoom.assignment.exceptions.TaskNotFoundException;
import com.codesoom.assignment.repositories.InMemoryTaskRepositoryImpl;
import com.codesoom.assignment.services.TaskDeleteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskDeleteControllerTest {

    private TaskDeleteController controller;

    private static final Long EXIST_ID = 1L;
    private static final Task SAVED_TASK = new Task(EXIST_ID, "title");
    private static final Long NOT_EXIST_ID = 100L;

    @BeforeEach
    void setup() {
        final InMemoryTaskRepositoryImpl repository = new InMemoryTaskRepositoryImpl();
        repository.save(SAVED_TASK);

        final TaskDeleteServiceImpl service = new TaskDeleteServiceImpl(repository);
        this.controller = new TaskDeleteController(service);
    }

    @DisplayName("할 일을 성공적으로 삭제한다.")
    @Test
    void deleteTask() {
        controller.deleteTask(EXIST_ID);
    }

    @DisplayName("존재하지 않는 할 일의 삭제 요청이 오면 예외가 발생한다.")
    @Test
    void thrownNotFoundException() {
        assertThrows(TaskNotFoundException.class, () -> controller.deleteTask(NOT_EXIST_ID));
    }

}
