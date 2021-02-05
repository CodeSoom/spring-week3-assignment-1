package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {

    private static TaskService taskService;
    private static final String TASK_TITLE = "title";
    private static final String UPDATE_POSTFIX = "-changed";

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);
    }

    @Test
    @DisplayName("할 일 목록의 갯수를 리턴한다")
    void getTasks() {

        List<Task> tasks = taskService.getTasks();
        assertThat(tasks).hasSize(1);
    }

    @Test
    @DisplayName("새로운 할 일을 만들고 할일 목록의 갯수를 전과 비교한")
    void createTask() {

        int oldSize = taskService.getTasks().size();

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);

        int newSize = taskService.getTasks().size();

        assertThat(newSize - oldSize).isEqualTo(1);
    }

    @Test
    @DisplayName("유효한 Id의 할 일을 리턴받아 타이틀을 비교한다")
    void getTask() {

        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    @DisplayName("유효하지 않은 Id로 할 일 목록을 검색 시 에러를 리턴한다")
    void getTaskByInvalidId() {

        assertThatThrownBy(() -> taskService.getTask(0L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("기존의 Id로 할 일의 타이틀을 수정한다")
    void updateTask() {

        Task task = new Task();
        task.setTitle(TASK_TITLE + UPDATE_POSTFIX);

        Task updatedTask = taskService.updateTask(1L, task);
        assertThat(updatedTask.getTitle()).isEqualTo(TASK_TITLE + UPDATE_POSTFIX);
    }

    @Test
    @DisplayName("유효한 Id로 할 일을 삭제하면 기존 할 일 목록의 갯수가 줄어든다")
    void deleteTask() {

        int oldSize = taskService.getTasks().size();

        taskService.deleteTask(1L);
        int newSize = taskService.getTasks().size();

        assertThat(oldSize - newSize).isEqualTo(1);
    }
}
