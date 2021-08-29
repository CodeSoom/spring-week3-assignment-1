package com.codesoom.assignment.application;

import com.codesoom.assignment.exceptions.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@DisplayName("Task 관리 서비스 테스트")
class TaskServiceTest {
    public static final String TEST_ONE_TITLE = "test1";
    public static final String TEST_TWO_TITLE = "test2";
    public static final long ID_ONE = 1L;
    public static final long ID_TWO = 2L;
    public static final String UPDATE_TITLE = "updateTitle";
    private TaskService taskService;
    private IdGenerator<Long> idGenerator;

    @BeforeEach
    void setUp() {
        idGenerator = spy(new TaskIdGenerator());
        taskService = new TaskService(idGenerator);
        TaskService.clear();
    }


    @DisplayName("저장된 할 일이 없는경우 비어있는 목록이 반환됩니다.")
    @Test
    void getTasksEmpty() {
        final List<Task> tasks = taskService.getTasks();

        assertThat(tasks).isEmpty();
    }

    @DisplayName("저장된 할 일이 있는 경우 저장된 할 일들이 반환됩니다.")
    @ParameterizedTest
    @MethodSource("provideTaskList")
    void getTasks(List<Task> sourceTasks, int size) {
        sourceTasks.forEach(taskService::createTask);

        final List<Task> tasks = taskService.getTasks();

        assertThat(tasks).hasSize(size);
    }

    public static Stream<Arguments> provideTaskList() {
        return Stream.of(
                Arguments.of(Arrays.asList(Task.from("test1"), Task.from("test2")), 2),
                Arguments.of(Arrays.asList(Task.from("test1"), Task.from("test2"), Task.from("test3")), 3)
        );
    }

    @DisplayName("존재하지 않는 아이디를 검색하면 예외가 발생합니다.")
    @Test
    void getTaskInvalidId() {
        assertThatThrownBy(()-> taskService.getTask(ID_ONE))
                .isInstanceOf(TaskNotFoundException.class);

    }

    @DisplayName("할 일을 저장하면 새로운 아이디가 발급되어 저장됩니다.")
    @Test
    void createTask() {
        final Task savedTask = taskService.createTask(Task.from(TEST_ONE_TITLE));
        final Task foundTask = taskService.getTask(ID_ONE);

        assertThat(foundTask).isEqualTo(savedTask);

        assertThat(foundTask.getTitle()).isEqualTo(TEST_ONE_TITLE);

        verify(idGenerator).generate(any());
    }


    @DisplayName("할 일의 내용을 수정할 수 있습니다.")
    @ParameterizedTest
    @MethodSource("provideTaskAndNewTitle")
    void updateTask(Task sourceTask, Task targetTask) {
        taskService.createTask(sourceTask);
        final Task updatedTask = taskService.updateTask(ID_ONE, targetTask);

        final Task findTask = taskService.getTask(ID_ONE);

        assertThat(findTask).isEqualTo(updatedTask);
        assertThat(findTask.getTitle()).isEqualTo(targetTask.getTitle());

    }

    public static Stream<Arguments> provideTaskAndNewTitle() {
        return Stream.of(
                Arguments.of(Task.from(TEST_ONE_TITLE), Task.from(UPDATE_TITLE)),
                Arguments.of(Task.from(TEST_TWO_TITLE), Task.from(UPDATE_TITLE))
        );
    }


    @DisplayName("할 일을 삭제할 수 있습니다.")
    @Test
    void deleteTask() {
        final Task savedTask = taskService.createTask(Task.from(TEST_ONE_TITLE));
        final Task foundTask = taskService.getTask(savedTask.getId());
        List<Task> tasks = taskService.getTasks();

        assertThat(foundTask).isEqualTo(savedTask);
        assertThat(tasks).isNotNull().hasSize(1);

        taskService.deleteTask(foundTask.getId());
        tasks = taskService.getTasks();

        assertThat(tasks).isEmpty();
        assertThatThrownBy(()->taskService.getTask(savedTask.getId()))
                .isInstanceOf(TaskNotFoundException.class);
    }

}
