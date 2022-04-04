package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("할 일에 대한 어플리케이션 로직 테스트")
class TaskServiceTest {

    private static final String TEST_TASK_TITLE = "TITLE";
    private static final String TEST_TASK_UPDATE_TITLE = "TITLE_UPDATE";

    private static final Long NOT_FOUND_TASK_ID = 999L;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Test
    @DisplayName("할 일 저장")
    void createTask() {
        //given
        Task source = new Task();
        source.setTitle(TEST_TASK_TITLE);

        //when
        Task task = taskService.createTask(source);

        //then
        assertAll(
                () -> assertThat(task.getId()).isNotNull(),
                () -> assertThat(task.getTitle()).isEqualTo(TEST_TASK_TITLE)
        );
    }

    @Test
    @DisplayName("할 일 목록 조회")
    void getTasks() {
        //given
        int taskCount = 5;
        IntStream.rangeClosed(1, taskCount)
                .forEach((index) -> generateTask());

        //when
        List<Task> tasks = taskService.getTasks();

        //then
        assertThat(tasks.size()).isEqualTo(taskCount);
    }

    @Test
    @DisplayName("할 일 상세 조회")
    void getTask() {
        //given
        Task givenTask = generateTask();

        //when
        Task foundTask = taskService.getTask(givenTask.getId());

        //then
        assertThat(foundTask).isEqualTo(givenTask);
    }

    @Test
    @DisplayName("할 일 상세 조회시 예외")
    void getTask_TaskNotFound_Exception() {
        assertThatThrownBy(
                () -> taskService.getTask(NOT_FOUND_TASK_ID)
        ).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("할 일 변경")
    void updateTask() {
        //given
        Task task = generateTask();

        Task updateSource = new Task();
        updateSource.setTitle(TEST_TASK_UPDATE_TITLE);

        //when
        Task updatedTask = taskService.updateTask(task.getId(), updateSource);

        //then
        assertThat(updatedTask.getTitle()).isEqualTo(TEST_TASK_UPDATE_TITLE);
    }

    @Test
    @DisplayName("할 일 변경시 예외")
    void updateTask_TaskNotFound_Exception() {
        //given
        Task source = new Task();
        source.setTitle(TEST_TASK_UPDATE_TITLE);

        //when
        //then
        assertThatThrownBy(() -> taskService.updateTask(NOT_FOUND_TASK_ID, source))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("할일 삭제")
    void deleteTask() {
        //given
        Task givenTask = generateTask();

        //when
        Task deletedTask = taskService.deleteTask(givenTask.getId());

        //then
        List<Task> findTasks = taskService.getTasks();

        assertAll(
                () -> assertThat(deletedTask).isEqualTo(givenTask),
                () -> assertThat(findTasks.size()).isEqualTo(0)
        );
    }

    @Test
    @DisplayName("할 일 삭제시 예외")
    void deleteTask_TaskNotFound_Exception() {
        assertThatThrownBy(
                () -> taskService.deleteTask(NOT_FOUND_TASK_ID)
        ).isInstanceOf(TaskNotFoundException.class);
    }

    private Task generateTask() {
        Task givenSource = new Task();
        givenSource.setTitle(TEST_TASK_TITLE);
        return taskService.createTask(givenSource);
    }
}