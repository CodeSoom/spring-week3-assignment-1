package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskControllerTest {
    private TaskController taskController;
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);

        Task task = new Task();
        task.setTitle("New Title");
        taskService.createTask(task);
    }


    @Test
    @DisplayName("Task 목록이 존재하는 경우에 점검")
    void list() {
        List<Task> list = taskController.list();
        assertThat(list)
                .as("Task List NotNull Test")
                .isNotNull();

        assertThat(list)
                .as("Task List NotEmpty Test")
                .isNotEmpty();

        assertThat(list)
                .as("Task List Size Test")
                .hasSize(1);

        assertThat(list.get(0).getTitle())
                .as("Task List index : 0 -> Tittle Equals Test")
                .isEqualTo("New Title");
        assertThat(list.get(0).getId()).isEqualTo(1L);

        assertThat(list)
                .as("Task List  Tittle Equals Test1")
                .filteredOn(task -> task.getTitle().equals("New Title"));

        assertThat(list)
                .as("Task List  Tittle Equals Test2")
                .extracting("title")
                .contains("New Title");
    }

    @Test
    @DisplayName("Task 목록에서 ID로 정상 조회시 점검")
    void detailWithValid() {
        Task detail = taskController.detail(1L);
        assertThat(detail)
                .as("detailWidValid isNotNull Test")
                .isNotNull();

        assertThat(detail.getTitle())
                .as("getTitle Compare Test1")
                .isEqualTo("New Title");

        assertThat(detail)
                .as("getTitle Compare Test2")
                .extracting("title")
                .isEqualTo("New Title");
    }

    @Test
    @DisplayName("Task 목록에서 ID로 정상 조회가 되지 않을 경우 점검")
    void detailWithInvalid() {
        assertThatThrownBy(() -> taskController.detail(2L))
                .as("getTask Exception Test")
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found: 2")
                .hasStackTraceContaining("TaskNotFoundException");
    }

    @Test
    @DisplayName("New Task 생성")
    void createTask() {
        Task task = new Task();
        task.setTitle("Second Title");
        taskController.create(task);
        List<Task> list = taskController.list();

        assertThat(list)
                .as("create Task 이후 Task List NotNull Test")
                .isNotNull();

        assertThat(list)
                .as("create Task 이후 Task List NotEmpty Test")
                .isNotEmpty();

        assertThat(list)
                .as("create Task 이후 Task List Size Test")
                .hasSize(2);

        assertThat(list)
                .as("create Task 이후 신규 Title 명으로 조회 Test1")
                .filteredOn(x -> x.getTitle().equals("Second Title"))
                .hasSize(1);

        assertThat(list)
                .as("create Task 이후 신규 ID 으로 조회 Test1")
                .filteredOn(x -> x.getId().equals(2L))
                .hasSize(1);

        assertThat(list)
                .as("삽입 Task와 반환.Task 객체 비교")
                .doesNotContain(task);

        assertThat(list.get(0).getTitle())
                .as("create Task 이후 신규 Title명으로 조회 Test2")
                .isEqualTo("New Title");

        assertThat(list.get(0).getId())
                .as("create Task 이후 기존 ID 으로 조회 Test1")
                .isEqualTo(1L);

        assertThat(list)
                .as("create Task Title 포함목록 조회 Test")
                .extracting("title")
                .contains("New Title", "Second Title");
    }

    @Test
    @DisplayName("Task ID가 있는 경우 업데이트 확인")
    void updateWithValid() {
        Task task = new Task();
        task.setTitle("Modify Task");

        assertThat(taskController.list())
                .as("Update 이전 list 사이즈 조회")
                .hasSize(1);

        assertThat(taskController.list())
                .as("Update 이전  Task Title 조회")
                .extracting("title")
                .contains("New Title");

        assertThat(taskController.list())
                .as("Update 이전 Task ID 조회")
                .filteredOn(x -> x.getId().equals(1L))
                .hasSize(1);

        taskController.update(1L, task);

        assertThat(taskController.list())
                .as("Task Update 이후 list 사이즈 조회")
                .hasSize(1);

        assertThat(taskController.list())
                .as("Task Update 이후 타이틀 조회 Test")
                .extracting("title")
                .contains("Modify Task");

        assertThat(taskController.list())
                .as("Task Update 이후 타이틀 조회 Test")
                .extracting("Id")
                .contains(1L);
    }

    @Test
    @DisplayName("Task ID가 없는 경우 Task 업데이트 확인")
    void updateWithInvalid() {
        Task task = new Task();
        task.setTitle("Modify Task");

        assertThatThrownBy(() -> taskController.update(2L, task))
                .as("Task ID가 없을 경우 Update 확인")
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found: 2")
                .hasStackTraceContaining("TaskNotFoundException");
    }

    @Test
    @DisplayName("Task ID가 있는 경우 Task 삭제")
    void deleteWithValid() {
        assertThat(taskController.list())
                .as("Task 삭제전 Task List 조회")
                .hasSize(1);

        assertThat(taskController.list())
                .as("Task 삭제전 Task ID로 조회")
                .filteredOn(x -> x.getId().equals(1L))
                .hasSize(1);

        assertThat(taskController.list())
                .as("Task 삭제전 Task Title로 조회")
                .extracting("title")
                .contains("New Title");

        taskController.delete(1L);

        assertThat(taskController.list())
                .as("Task 삭제후 TaskList 사이즈 조회")
                .hasSize(0);

        assertThat(taskController.list())
                .as("Task 삭제 후 Task ID로 조회 사이즈 Test")
                .filteredOn(x -> x.getId().equals(1L))
                .hasSize(0);

        assertThat(taskController.list())
                .as("Task 삭제 후 Task Title로 조회 사이즈 Test")
                .filteredOn(x -> x.getTitle().equals("New Title"))
                .hasSize(0);
    }

    @Test
    @DisplayName("Task ID가 없는 경우 Task 삭제")
    void deleteWithInvalid() {
        assertThatThrownBy(() -> taskController.delete(2L))
                .as("Task ID가 없을 경우 Delete 확인")
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found: 2")
                .hasStackTraceContaining("TaskNotFoundException");
    }

}