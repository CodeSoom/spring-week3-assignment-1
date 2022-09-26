package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Order(1)
    @Test
    @DisplayName("빈 DB에서 전체 할 일 조회")
    void get_empty_tasks() {
        List<Task> findTasks = taskService.getTasks();

        assertThat(findTasks).hasSize(0);
        assertThat(findTasks).isEqualTo(new ArrayList<>());

    }

    @Order(2)
    @Test
    @DisplayName("새로운 할 일 2개 추가 ")
    void create_task() {
        // given
        Task task1 = new Task();
        task1.setTitle("할 일 추가1");

        Task task2 = new Task();
        task2.setTitle("할 일 추가2");

        // when
        taskService.createTask(task1);
        taskService.createTask(task2);

        // then
        assertThat(taskService.getTasks()).hasSize(2);
        assertThat(taskService.getTask(1L).getTitle()).isEqualTo("할 일 추가1");
        assertThat(taskService.getTask(2L).getTitle()).isEqualTo("할 일 추가2");
    }

    @Order(3)
    @Test
    @DisplayName("2개 할 일 추가된 후 전체 할 일 조회")
    void get_not_empty_tasks() {
        if (taskService.getTasks().size() == 0) {
            // given
            Task task1 = new Task();
            task1.setTitle("할 일 추가1");

            Task task2 = new Task();
            task2.setTitle("할 일 추가2");

            taskService.createTask(task1);
            taskService.createTask(task2);
        }

        List<Task> findTasks = taskService.getTasks();

        final int expectSize = 2;
        final int actualSize = findTasks.size();

        assertThat(actualSize).isEqualTo(expectSize);
    }


    @Order(4)
    @Test
    @DisplayName("존재하는 ID의 할 일 조회")
    void get_task_with_existing_taskId() {
        // given
        if (taskService.getTasks().size() == 0) {
            Task task = new Task();
            task.setTitle("할 일 추가1");

            taskService.createTask(task);
        }

        // when
        Task findTask = taskService.getTask(1L);

        // then
        assertThat(findTask.getTitle()).isEqualTo("할 일 추가1");
    }

    @Order(5)
    @Test
    @DisplayName("존재하지않는 ID의 할 일 조회")
    void get_task_with_not_existing_taskId() {
        assertThatThrownBy(() -> taskService.getTask(100L)).isInstanceOf(TaskNotFoundException.class);
    }

    @Order(6)
    @Test
    @DisplayName("존재하는 ID의 할 일 수정")
    void update_task_with_existing_taskId() {
        if (taskService.getTasks().size() == 0) {
            Task task = new Task();
            task.setTitle("할 일 추가1");

            taskService.createTask(task);
        }

        Task newTask = new Task();
        newTask.setTitle("할 일 수정1");

        Task updatedTask = taskService.updateTask(1L, newTask);

        assertThat(updatedTask.getTitle()).isEqualTo(newTask.getTitle());
    }

    @Order(7)
    @Test
    @DisplayName("존재하지않는 ID의 할 일 수정")
    void update_task_with_not_existing_taskId() {
        Task newTask = new Task();
        newTask.setTitle("할 일 수정100");

        assertThatThrownBy(() -> taskService.updateTask(100L, newTask)).isInstanceOf(TaskNotFoundException.class);
    }

    @Order(8)
    @Test
    @DisplayName("존재하는 ID의 할 일 삭제")
    void delete_task_with_existing_taskId() {
        if (taskService.getTasks().size() == 0) {
            Task task = new Task();
            task.setTitle("할 일 추가1");

            taskService.createTask(task);
        }

        final int beforeTotalCount = taskService.getTasks().size();
        final Task expectedTask = taskService.getTask(1L);

        Task deletedTask = taskService.deleteTask(1L);

        final int afterTotalCount = taskService.getTasks().size();

        assertThat(afterTotalCount).isEqualTo(beforeTotalCount - 1);
        assertThat(deletedTask).isEqualTo(expectedTask);
    }

    @Order(9)
    @Test
    @DisplayName("존재하지않는 ID의 할 일 삭제")
    void delete_task_with_not_existing_taskId() {
        assertThatThrownBy(() -> taskService.deleteTask(100L)).isInstanceOf(TaskNotFoundException.class);
    }
}