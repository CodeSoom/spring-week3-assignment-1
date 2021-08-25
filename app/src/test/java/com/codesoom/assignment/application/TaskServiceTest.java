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

@DisplayName("Task 관리 서비스 테스트")
class TaskServiceTest {
    public static final String TEST_ONE_TITLE = "test1";
    public static final String TEST_TWO_TITLE = "test2";
    public static final long ID_ONE = 1L;
    public static final long ID_TWO = 2L;
    public static final String UPDATE_TITLE = "updateTitle";
    private TaskService taskService;
    private final IdGenerator<Long> idGenerator = new TaskIdGenerator();

    @BeforeEach
    void setUp() {
        taskService = new TaskService(idGenerator);
        TaskService.clear();
    }


    @DisplayName("저장된 할 일이 없는경우 빈 컬렉션이 반환됩니다.")
    @Test
    void getTasksEmpty() {
        final List<Task> tasks = taskService.getTasks();

        assertThat(tasks).isEmpty();
    }

    @DisplayName("저장된 할 일이 있는 경우 저장된 할 일들이 반환됩니다.")
    @ParameterizedTest
    @MethodSource("provideTaskList")
    void getTasks(List<Task> tasks, int size) {
        tasks.forEach(taskService::createTask);

        final List<Task> response = taskService.getTasks();

        assertThat(response).hasSize(size);
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
        final Task savedTask1 = taskService.createTask(Task.from(TEST_ONE_TITLE));
        final Task savedTask2 = taskService.createTask(Task.from(TEST_TWO_TITLE));

        final Task findTask1 = taskService.getTask(ID_ONE);
        final Task findTask2 = taskService.getTask(ID_TWO);

        assertThat(findTask1).isEqualTo(savedTask1);
        assertThat(findTask2).isEqualTo(savedTask2);

        assertThat(findTask1.getTitle()).isEqualTo(TEST_ONE_TITLE);
        assertThat(findTask2.getTitle()).isEqualTo(TEST_TWO_TITLE);
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
        final Task findTask = taskService.getTask(savedTask.getId());
        List<Task> tasks = taskService.getTasks();

        assertThat(findTask).isEqualTo(findTask);
        assertThat(tasks).isNotNull().hasSize(1);

        taskService.deleteTask(findTask.getId());
        tasks = taskService.getTasks();

        assertThat(tasks).isEmpty();
        assertThatThrownBy(()->taskService.getTask(savedTask.getId()))
                .isInstanceOf(TaskNotFoundException.class);
    }

}
