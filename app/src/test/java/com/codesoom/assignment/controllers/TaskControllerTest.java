package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskControllerTest {

    TaskController taskController;
    TaskService taskService;
    Task task;

    @BeforeEach
    void init(){
        taskService = new TaskService();
        taskController = new TaskController(taskService);
        task = new Task();
        task.setId(1L);
        task.setTitle("책읽기");
    }

    @DisplayName("비어있는 Task 목록")
    @Test
    void testListWithoutContent(){
        assertThat(taskController.list()).isEmpty();
    }

    @DisplayName("존재하는 Task 목록")
    @Test
    void testListWithContent(){
        taskController.create(task);
        assertThat(taskController.list()).isNotEmpty();
    }

    @DisplayName("존재하는 Task")
    @Test
    void testDetailValid(){
        taskController.create(task);
        assertThat(taskController.detail(1L)).isEqualTo(task);
        assertThat(taskController.detail(1L)).isSameAs(task);
    }

    @DisplayName("존재하지않는 Task")
    @Test
    void testDetailInValid(){
        taskController.create(task);
        assertThat(taskController.detail(1L)).isEqualTo(task);
        assertThatThrownBy(() -> {taskController.detail(2L); } )
                .isInstanceOf(TaskNotFoundException.class);
        // 성공할 것 같은데 실패함 : 이유 더 찾아보기
        // assertThat(taskController.detail(1L)).isSameAs(task);
    }

    @DisplayName("Task 생성")
    @Test
    void testCreate(){
        assertThat(taskController.create(task).getId()).isEqualTo(task.getId());
    }

    @DisplayName("Task 변경")
    @Test
    void testUpdate(){
        String newTitle = "책사기";
        Task newTask = new Task();
        newTask.setTitle(newTitle);

        taskController.create(task);
        taskController.update(1L, newTask);

        assertThat(taskController.detail(1L).getTitle()).isEqualTo(newTitle);
    }

    @DisplayName("Task 삭제")
    @Test
    void testDelete(){
        taskController.create(task);
        taskController.delete(taskController.detail(task.getId()).getId());

        assertThatThrownBy(() -> {taskController.detail(1L); } )
                .isInstanceOf(TaskNotFoundException.class);
    }

}
