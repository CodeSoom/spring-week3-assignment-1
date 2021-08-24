package com.codesoom.assignment.application;

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

@DisplayName("Task 관리 서비스 테스트")
class TaskServiceTest {
    public static final String TEST_ONE_TITLE = "test1";
    public static final String TEST_TWO_TITLE = "test2";
    private TaskService taskService;
    private final IdGenerator<Long> idGenerator = new TaskIdGenerator();

    @BeforeEach
    void setUp() {
        taskService = new TaskService(idGenerator);
    }


    @DisplayName("저장된 할 일이 없는경우 빈 컬렉션이 반환된다.")
    @Test
    void getTasksEmpty() {
        final List<Task> tasks = taskService.getTasks();

        assertThat(tasks).isEmpty();
    }

    @DisplayName("저장된 할 일이 있는 경우 저장된 할 일들이 반환된다.")
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


    @DisplayName("할 일을 저장하면 새로운 아이디가 발급되어 저장된다.")
    @Test
    void createTask() {
        final Task savedTask1 = taskService.createTask(Task.from(TEST_ONE_TITLE));
        final Task savedTask2 = taskService.createTask(Task.from(TEST_TWO_TITLE));

        final Task findTask1 = taskService.getTask(1L);
        final Task findTask2 = taskService.getTask(2L);

        assertThat(findTask1).isEqualTo(savedTask1);
        assertThat(findTask2).isEqualTo(savedTask2);

        assertThat(findTask1.getTitle()).isEqualTo(TEST_ONE_TITLE);
        assertThat(findTask2.getTitle()).isEqualTo(TEST_TWO_TITLE);
    }





}
