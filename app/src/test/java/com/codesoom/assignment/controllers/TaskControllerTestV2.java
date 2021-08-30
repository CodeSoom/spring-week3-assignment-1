package com.codesoom.assignment.controllers;


import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.exceptions.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("spy를 이용한 TaskController 테스트")
class TaskControllerTestV2 {

    private TaskService taskService;
    private TaskController taskController;

    private static final String TASK_TITLE_ONE = "test1";
    private static final String TASK_TITLE_TWO = "test2";
    private static final String TASK_UPDATE_TITLE = "updateTitle";
    private Task task1;
    private Task task2;

    @BeforeEach
    void setup() {
        taskService = mock(TaskService.class);
        taskController = new TaskController(taskService);

        TaskService.clear();
        //task1 = taskService.createTask(Task.from(TASK_TITLE_ONE));
        //task2 = taskService.createTask(Task.from(TASK_TITLE_TWO));
        task1 = new Task(1L, TASK_TITLE_ONE);
        task2 = new Task(2L, TASK_TITLE_TWO);

        given(taskService.getTasks()).willReturn(new ArrayList<>());

        given(taskService.getTask(task1.getId())).willReturn(task1);
        given(taskService.getTask(task2.getId())).willReturn(task2);
        given(taskService.getTask(10L))
                .willThrow(TaskNotFoundException.class);
        given(taskService.updateTask(eq(10L), any(Task.class)))
                .willThrow(TaskNotFoundException.class);

        given(taskService.deleteTask(10L))
                .willThrow(TaskNotFoundException.class);

    }



    @DisplayName("할 일이 없으면 빈 리스트가 조회됩니다.")
    @Test
    void listWithoutEntity() {
        assertThat(taskController.list()).isEmpty();

        verify(taskService).getTasks();
    }

    @DisplayName("아이디를 통해 할 일을 조회 할 수 있습니다.")
    @Test
    void detail() {
        assertThat(taskController.detail(task1.getId())).isEqualTo(task1);
        assertThat(taskController.detail(task2.getId())).isEqualTo(task2);

        verify(taskService).getTask(task1.getId());
        verify(taskService).getTask(task2.getId());
    }

    @DisplayName("유효하지 않은 아이디로 할 일을 검색하면 예외가 발생합니다.")
    @Test
    void detailInvalid() {
        assertThatThrownBy(()-> taskController.detail(10L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @DisplayName("새로운 할 일을 등록할 수 있습니다.")
    @Test
    void createTask() {
        taskController.create(task1);

        verify(taskService).createTask(task1);
    }

    @DisplayName("할 일을 수정할 수 있습니다.")
    @Test
    void updateTask() {
        taskController.update(task1.getId(), Task.from(TASK_UPDATE_TITLE));

        verify(taskService).updateTask(task1.getId(), Task.from(TASK_UPDATE_TITLE));
    }

    @DisplayName("존재하지 않는 식별자의 할 일을 수정하려 할 경우 예외가 발생합니다.")
    @Test
    void updateTaskInvalid() {
        assertThatThrownBy(()-> taskController.update(10L, Task.from(TASK_UPDATE_TITLE)))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @DisplayName("할 일을 수정할 수 있습니다.")
    @Test
    void patchTask() {
        taskController.patch(task1.getId(), Task.from(TASK_UPDATE_TITLE));

        verify(taskService).updateTask(task1.getId(), Task.from(TASK_UPDATE_TITLE));

    }

    @DisplayName("존재하지 않는 식별자의 할 일을 수정하려 할 경우 예외가 발생합니다.")
    @Test
    void patchTaskInvalid() {
        assertThatThrownBy(()-> taskController.patch(10L, Task.from(TASK_UPDATE_TITLE)))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @DisplayName("할 일을 삭제할 수 있습니다.")
    @Test
    void deleteTask() {
        taskController.delete(task1.getId());

        verify(taskService).deleteTask(task1.getId());
    }

    @DisplayName("존재하지 않는 할 일을 삭제하려 하면 예외가 발생합니다. ")
    @Test
    void deleteTaskInvalid() {
        assertThatThrownBy(()-> taskController.delete(10L))
                .isInstanceOf(TaskNotFoundException.class);
    }


}
