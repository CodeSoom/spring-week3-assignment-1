package com.codesoom.assignment.controllers;

import com.codesoom.assignment.common.exceptions.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.services.TaskService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class TaskControllerTest {

    private TaskController controller;
    private TaskService taskService;

    private final Long NOT_FOUND_TASK_ID = 100L; // 목록에 없는 할 일 ID
    private final Long NEW_TASK_ID = 1L; // 새로 생성할 할 일 ID
    private final String NEW_TASK_TITLE = "Test Title"; // 새로 생성할 할 일 제목
    private final String UPDATE_TASK_TITLE = "Test Title Updated"; // 수정된 할 일 제목
    private final String TASK_NOT_FOUND_ERROR_MESSAGE = "해당하는 Task가 존재하지 않습니다.";

    @BeforeEach
    void setUp(){

        taskService = new TaskService();
        controller = new TaskController(taskService);

    }

    @Test
    @DisplayName("빈 할 일 목록을 조회합니다.")
    void listEmpty() {

        //given

        //when
        List<Task> taskList = controller.list();

        //then
        Assertions.assertThat(taskList).isEmpty();
        Assertions.assertThat(taskList).hasSize(0);

    }
    
    @Test
    @DisplayName("비어있지 않은 할 일 목록을 조회합니다.")
    void list() {

        //given
        Task task1 = new Task();
        Task task2 = new Task();
        taskService.saveTask(task1);
        taskService.saveTask(task2);

        //when
        List<Task> taskList = controller.list();

        //then
        Assertions.assertThat(taskList).isNotEmpty().hasSize(2);

    }

    @Test
    @DisplayName("할 일 목록에 없는 할 일을 조회 할 때 에러를 확인합니다.")
    void detailInValid() {

        //given
        Task newTask = new Task();

        //when
        taskService.saveTask(newTask);

        //then
        Assertions.assertThatThrownBy( () -> controller.detail(NOT_FOUND_TASK_ID))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining(TASK_NOT_FOUND_ERROR_MESSAGE);

    }

    @Test
    @DisplayName("할 일 목록에 있는 할 일을 조회합니다.")
    void detailValid() {

        //given
        Task newTask = new Task();
        newTask.setTitle(NEW_TASK_TITLE);

        Long foundTaskId = NEW_TASK_ID; // 조회할 할 일 ID

        //when
        taskService.saveTask(newTask);
        Task foundTask = controller.detail(foundTaskId);

        //then
        Assertions.assertThat(foundTask).isNotNull();
        Assertions.assertThat(foundTask.getId()).isEqualTo(foundTaskId);
        Assertions.assertThat(foundTask.getTitle()).isEqualTo(NEW_TASK_TITLE);

    }

    @Test
    @DisplayName("새 할 일을 생성하여 할 일 목록에 저장합니다.")
    void create() {

        //given
        Task task1 = new Task();
        task1.setTitle(NEW_TASK_TITLE);

        Long newTaskId = NEW_TASK_ID; // 생성할 할 일 ID

        //when
        controller.create(task1);
        Task foundTask = controller.detail(newTaskId);

        //then
        Assertions.assertThat(controller.list()).isNotEmpty().hasSize(1);

        Assertions.assertThat(foundTask).isNotNull();
        Assertions.assertThat(foundTask.getId()).isEqualTo(newTaskId);
        Assertions.assertThat(foundTask.getTitle()).isEqualTo(NEW_TASK_TITLE);

    }

    @Test
    @DisplayName("할 일 목록에 없는 할 일을 수정 할 때 에러를 확인합니다.")
    void updateInvalid() {

        //given
        Long updateTaskId = NEW_TASK_ID; // 수정할 할 일 ID

        Task paramTask = new Task(); // 파라미터로 사용될 할 일 객체
        paramTask.setTitle(UPDATE_TASK_TITLE);

        //when
        //then
        Assertions.assertThatThrownBy( () -> controller.update(updateTaskId, paramTask))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining(TASK_NOT_FOUND_ERROR_MESSAGE);

    }

    @Test
    @DisplayName("할 일 목록에 있는 할 일을 수정합니다.")
    void updateValid() {

        //given
        Task newTask = new Task();
        newTask.setTitle(NEW_TASK_TITLE);
        Task targetTask = controller.create(newTask);

        Long updateTaskId = targetTask.getId(); // 수정할 할 일 ID

        Task paramTask = new Task(); // 파라미터로 사용될 할 일 객체
        paramTask.setTitle(UPDATE_TASK_TITLE);

        //when
        Task updatedTask = controller.update(updateTaskId, paramTask);

        //then
        Assertions.assertThat(updatedTask).isNotNull();
        Assertions.assertThat(updatedTask.getId()).isEqualTo(updateTaskId);
        Assertions.assertThat(updatedTask.getTitle()).isEqualTo(UPDATE_TASK_TITLE);

    }

    @Test
    @DisplayName("할 일 목록에 없는 할 일을 삭제 할 때 에러를 확인합니다.")
    void deleteInvalid() {

        //give
        //when
        //then
        Assertions.assertThatThrownBy( () -> controller.delete(NOT_FOUND_TASK_ID))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining(TASK_NOT_FOUND_ERROR_MESSAGE);

    }

    @Test
    @DisplayName("할 일 목록에 있는 할 일을 삭제합니다.")
    void deleteValid() {

        //give
        Task newTask = new Task();
        newTask.setTitle(NEW_TASK_TITLE);
        Task targetTask = controller.create(newTask);

        Long deleteTaskId = targetTask.getId(); // 삭제할 할 일 ID

        // 삭제 전에는 등록한 할 일이 존재합니다.
        Assertions.assertThat(controller.list()).isNotEmpty().hasSize(1);

        //when
        controller.delete(deleteTaskId);

        //then
        Assertions.assertThat(controller.list()).isEmpty();

    }

}