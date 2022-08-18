package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@AutoConfigureMockMvc
class TaskControllerTest {

    private final String DEFAULT_TITLE = "TEST";
    private final int DEFAULT_SIZE = 3;
    private TaskController controller;
    private TaskService service;

    @BeforeEach
    void setUp() {
        // subject
        service = new TaskService();
        controller = new TaskController(service);

        // fixtures
        for(long i = 1 ; i <= DEFAULT_SIZE ; i++){
            Task newTask = new Task();
            newTask.setTitle(DEFAULT_TITLE + i);
            service.createTask(newTask);
        }
    }

    @Test
    @DisplayName("모든 Task를 조회한다.")
    void list(){
        assertThat(controller.list()).hasSize(DEFAULT_SIZE);
    }

    @Test
    @DisplayName("존재하는 [id]로 Task를 조회한다.")
    void getTaskValidId() {
        long id = 3;
        assertThat(controller.detail(id)).isEqualTo(new Task((long) DEFAULT_SIZE, DEFAULT_TITLE + 3));
    }

    @Test
    @DisplayName("존재하지 않는 [id]로 조회하면 예외가 발생한다.")
    void getTaskInvalidId(){
        long id = DEFAULT_SIZE + 1;
        assertThatThrownBy(() -> controller.detail(id))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("신규 Task를 삽입한다.")
    void addTask(){
        Task newTask = new Task();
        newTask.setTitle(DEFAULT_TITLE + (DEFAULT_SIZE + 1));
        Task saveTask = controller.create(newTask);

        assertThat(newTask.getTitle()).isEqualTo(saveTask.getTitle());
        assertThat(controller.list()).hasSize(DEFAULT_SIZE + 1);
    }

    @Test
    @DisplayName("존재하지 않는 [id]로 삭제하면 예외가 발생한다.")
    void removeTaskInvalidId(){
        long id = DEFAULT_SIZE + 1;
        assertThatThrownBy(() -> controller.detail(id))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("존재하는 [id]로 삭제하면 tasks의 사이즈가 줄어든다.")
    void removeTaskValidId(){
        long id = DEFAULT_SIZE;
        int oldSize = controller.list().size();
        controller.delete(id);
        int newSize = controller.list().size();
        assertThat(oldSize).isEqualTo(newSize + 1);
    }
}
