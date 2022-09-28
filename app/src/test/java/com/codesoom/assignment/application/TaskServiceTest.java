package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {
    private final long INVALID_TASK_ID = 0L;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Nested
    @DisplayName("task리스트를 요청했을 때")
    class GetTasksTest {
        @Test
        @DisplayName("여러 task가 있는 경우 task를 모두 반환한다")
        void getTasks() {
            // given
            taskService.createTask(new Task(1L, "test1"));
            taskService.createTask(new Task(2L, "test2"));

            // when
            List<Task> tasks = taskService.getTasks();

            // then
            assertThat(tasks.size()).isEqualTo(2);
        }

        @Test
        @DisplayName("task가 없는 경우 빈 리스트를 반환한다")
        void getTaskWithEmptyTask() {
            // given
            // when
            List<Task> tasks = taskService.getTasks();

            // then
            assertThat(tasks.size()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("task를 상세조회 했을 때")
    class GetTaskTest {
        private Task testTask;

        @BeforeEach
        void setUp() {
            testTask = taskService.createTask(new Task(1L, "test1"));
        }

        @Test
        @DisplayName("유효한 task의 id로 task를 조회한 경우 task의 데이터를 반환한다")
        void getTaskWithValidId() {
            // given
            // when
            Long id = testTask.getId();
            Task task = taskService.getTask(id);

            // then
            assertThat(task.getId()).isEqualTo(id);
            assertThat(task.getTitle()).isEqualTo(testTask.getTitle());
        }

        @Test
        @DisplayName("유효하지 않은 task id로 task를 조회한 경우 TaskNotFoundException 예외를 던진다")
        void getTaskWithInvalidId() {
            assertThatThrownBy(
                    () -> taskService.getTask(INVALID_TASK_ID)
            ).isExactlyInstanceOf(TaskNotFoundException.class);
        }
    }


    @Nested
    @DisplayName("task를 생성할 때")
    class PostTaskTest {
        @Nested
        @DisplayName("기존에 없던 task를 생성하는 경우")
        class CreateNewTaskTest {
            @Test
            @DisplayName("새로운 title의 task를 생성 후 task의 데이터를 반환한다")
            void createTask() {
                // given
                Task newTask = new Task(null, "task1");

                // when
                Task createTask = taskService.createTask(newTask);

                // then
                assertThat(createTask.getId()).isEqualTo(1L);
                assertThat(createTask.getTitle()).isEqualTo(newTask.getTitle());
            }
        }

        @Nested
        @DisplayName("기존에 있던 task와 title이 같은 task를 생성하는 경우")
        class CreateNewTaskWithDuplicatedTitleTest {
            @BeforeEach
            void setUp() {
                taskService.createTask(new Task(1L, "task1"));
            }

            @Test
            @DisplayName("같은 title이 존재하는 경우 정상적으로 생성된다.")
            void createTaskWithDuplicatedTitle() {
                // given
                Task newTask = new Task(null, "task1");

                // when
                Task createTask = taskService.createTask(newTask);

                // then
                assertThat(createTask.getId()).isEqualTo(2L);
                assertThat(createTask.getTitle()).isEqualTo(newTask.getTitle());
            }
        }
    }


    @Nested
    @DisplayName("task를 수정할 때")
    class PutTaskTest {
        private Task testTask;

        @BeforeEach
        void setUp() {
            testTask = taskService.createTask(new Task(1L, "task1"));
        }

        @Test
        @DisplayName("유효한 task id로 task의 수정을 요청한 경우 task의 데이터를 수정 후 반환한다")
        void updateTaskWithValidId() {
            // given
            Long id = testTask.getId();
            Task task = new Task(null, "update Task");

            // when
            Task updateTask = taskService.updateTask(id, task);

            // then
            assertThat(updateTask.getId()).isEqualTo(id);
            assertThat(updateTask.getTitle()).isEqualTo(task.getTitle());
        }

        @Test
        @DisplayName("유효하지 않은 task id로 task의 수정을 요청한 경우 TaskNotFoundException 예외를 던진다")
        void updateTaskWithInvalidId() {
            Task task = new Task(null, "update Task");
            assertThatThrownBy(
                    () -> taskService.updateTask(INVALID_TASK_ID, task)
            ).isExactlyInstanceOf(TaskNotFoundException.class);
        }
    }


    @Nested
    @DisplayName("task를 삭제할 때")
    class DeleteTaskTest {
        private Task testTask;

        @BeforeEach
        void setUp() {
            testTask = taskService.createTask(new Task(1L, "task1"));
        }

        @Test
        @DisplayName("유효한 task id로 task의 삭제를 요청한 경우 task 데이터를 삭제한다")
        void deleteTaskWithValidId() {
            // given
            Long id = testTask.getId();

            // when
            taskService.deleteTask(id);

            // then
            assertThatThrownBy(
                    () -> taskService.getTask(id)
            ).isExactlyInstanceOf(TaskNotFoundException.class);
        }

        @Test
        @DisplayName("유효하지 않은 task id로 task의 삭제를 요청한 경우 TaskNotFoundException 예외를 던진다")
        void deleteTaskWithInvalidId() {
            assertThatThrownBy(
                    () -> taskService.deleteTask(INVALID_TASK_ID)
            ).isExactlyInstanceOf(TaskNotFoundException.class);
        }
    }
}