package com.codesoom.assignment.application;

import com.codesoom.assignment.BaseTaskTest;
import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest extends BaseTaskTest {

    private TaskService taskService;

    @BeforeEach()
    void setUp() {
        taskService = new TaskService();
    }

    @Test
    @DisplayName("신규 할일 번호 자동 생성")
    void generateNewTaskId() {
        Task firstTask = generateNewTask(TASK_TITLE_1);
        Task secondTask = generateNewTask(TASK_TITLE_2);

        Task firstCreatedTask = taskService.createTask(firstTask);
        Task secondCreatedTask = taskService.createTask(secondTask);

        Long idGap = secondCreatedTask.getId() - firstCreatedTask.getId();
        assertThat(idGap).isEqualTo(1L);
    }


    @Test
    @DisplayName("신규 할일 생성")
    void createTask() {
        Task newTask = generateNewTask( TASK_TITLE_1);

        Task created = taskService.createTask(newTask);

        assertThat(created.getId()).isEqualTo(TASK_ID_1);
        assertThat(created.getTitle()).isEqualTo(TASK_TITLE_1);
    }


    @Test
    @DisplayName("할일이 없을 때 목록 조회")
    void readEmptyTasks() {
        List<Task> tasks = taskService.getTasks();

        assertThat(tasks).hasSize(0);
    }

    @Test
    @DisplayName("할일이 있을 때 할일 목록 조회")
    void readTasks() {
        Task newTask = generateNewTask(TASK_TITLE_1);
        taskService.createTask(newTask);

        List<Task> tasks = taskService.getTasks();
        assertThat(tasks).hasSize(1);
    }

    @Test
    @DisplayName("존재하는 할일 조회")
    void readTask() {
        Task newTask = generateNewTask(TASK_TITLE_1);
        Task created = taskService.createTask(newTask);
        Task found = taskService.getTask(1L);

        assertThat(found).isNotNull();
        assertThat(found).isEqualTo(created);
    }

    @Test
    @DisplayName("존재하지 않은 할일 조회")
    void readNotExistTask() {
        assertThatThrownBy(() -> taskService.getTask(1L))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining(ERROR_MSG_TASK_NOT_FOUND);
    }

    @Test
    @DisplayName("존재하는 할일의 제목을 수정")
    void editExistTaskTitle() {
        Task newTask = generateNewTask(TASK_TITLE_1);
        taskService.createTask(newTask);

        Task editedTask = generateNewTask(TASK_TITLE_2);
        Task updatedTask = taskService.updateTask(TASK_ID_1, editedTask);

        assertThat(updatedTask.getTitle()).isNotEqualTo(TASK_TITLE_1);
        assertThat(updatedTask.getTitle()).isEqualTo(TASK_TITLE_2);
    }

    @Test
    @DisplayName("존재하지 않는 할일의 제목을 수정")
    void editNotExistTaskTitle() {
        assertThatThrownBy(() -> {
            Task editedTask = generateNewTask(TASK_TITLE_2);
            taskService.updateTask(TASK_ID_1, editedTask);
        }).isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining(ERROR_MSG_TASK_NOT_FOUND);
    }

    @Test
    @DisplayName("존재하는 할일을 삭제")
    void deleteExistTask() {
        Task newTask = generateNewTask(TASK_TITLE_1);
        Task created = taskService.createTask(newTask);

        Task deleted = taskService.deleteTask(created.getId());

        List<Task> tasks = taskService.getTasks();

        assertThat(tasks).doesNotContain(deleted);
    }

    @Test
    @DisplayName("존재하지 않는 할일을 삭제")
    void deleteNotExistTask() {
        assertThatThrownBy(() -> {
            taskService.deleteTask(TASK_ID_1);
        }).isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining(ERROR_MSG_TASK_NOT_FOUND);

    }

}
