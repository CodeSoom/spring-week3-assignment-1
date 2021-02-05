package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskController 를 테스트 한다.")
class TaskControllerTest {
    private static final String TASK_TITLE_1 = "title1";
    private static final String TASK_TITLE_2 = "title2";
    private Task task1;
    private Task task2;
    private List<Long> givenInvalidIds;

    private TaskController taskController;

    @BeforeEach
    void setUp() {
        TaskService taskService = new TaskService();
        taskController = new TaskController(taskService);

        task1 = new Task();
        task1.setTitle(TASK_TITLE_1);

        task2 = new Task();
        task2.setTitle(TASK_TITLE_2);

        givenInvalidIds = new ArrayList<>();
        Collections.addAll(givenInvalidIds, -100L, -1L, 0L, 5L, 100L);
    }

    @Test
    @DisplayName("할 일이 없는 경우 할 일 목록을 가져오면, 비어있는 리스트가 리턴된다.")
    void listWithEmptyTasks() {
        assertThat(taskController.list()).isEmpty();
    }

    @Test
    @DisplayName("할 일이 추가되는 경우 할 일 목록을 가져오면, 리턴되는 리스트의 크기가 증가한다.")
    void listWithAddedTasks() {
        taskController.create(task1);
        assertThat(taskController.list()).hasSize(1);

        taskController.create(task1);
        assertThat(taskController.list()).hasSize(2);

        taskController.create(task1);
        assertThat(taskController.list()).hasSize(3);
    }

    @Test
    @DisplayName("주어진 id를 가진 할 일이 저장되어 있는 경우 해당 할 일을 가져올 수 있다.")
    void detailWithValidId() {
        taskController.create(task1);
        taskController.create(task2);

        assertThat(taskController.detail(1L).getTitle()).isEqualTo(task1.getTitle());
        assertThat(taskController.detail(2L).getTitle()).isEqualTo(task2.getTitle());
    }

    @Test
    @DisplayName("주어진 id를 가진 할 일이 없는 경우 해당 할 일을 가져오려고 하면, NotFound 예외가 발생한다.")
    void detailWithInvalidId() {
        taskController.create(task1);
        taskController.create(task2);

        for (long id : givenInvalidIds) {
            Assertions.assertThrows(
                    TaskNotFoundException.class,
                    () -> taskController.detail(id),
                    "할 일을 찾을 수 없을 경우를 표현하는 예외가 던져져야 한다."
            );
        }
    }

    @Test
    @DisplayName("할 일이 없는 경우 할 일을 추가하면, 비어있지 않은 리스트가 리턴된다.")
    void createNewTask() {
        taskController.create(task1);

        assertThat(taskController.list()).isNotEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 할 일을 추가하면, NPE 가 발생한다.")
    void createInvalidNewTask() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> taskController.create(null),
                "NullPointerException 이 던져져야 한다."
        );
    }

    @Test
    @DisplayName("주어진 id를 가진 할 일을 찾을 수 있고 변경할 수 있다면, 할 일이 변경된다.")
    void updateTaskWithValidId() {
        taskController.create(task1);

        task1.setTitle(TASK_TITLE_2);
        taskController.update(1L, task1);

        assertThat(taskController.detail(1L).getTitle()).isEqualTo(TASK_TITLE_2);
    }

    @Test
    @DisplayName("주어진 id를 가진 할 일을 찾을 수 없으면, NotFound 예외가 발생한다.")
    void updateTaskWithInvalidId() {
        taskController.create(task1);

        for (long id : givenInvalidIds) {
            Assertions.assertThrows(
                    TaskNotFoundException.class,
                    () -> taskController.update(id, task2),
                    "할 일을 찾을 수 없을 경우를 표현하는 예외가 던져져야 한다."
            );
        }
    }

    @Test
    @DisplayName("주어진 id를 가진 할 일이 저장되어 있지만 변경할 내용이 없다면, NPE 가 발생한다.")
    void updateTaskWithInvalidNewTask() {
        taskController.create(task1);

        Assertions.assertThrows(
                NullPointerException.class,
                () -> taskController.update(1L, null),
                "NullPointerException 이 던져져야 한다."
        );
    }
}
