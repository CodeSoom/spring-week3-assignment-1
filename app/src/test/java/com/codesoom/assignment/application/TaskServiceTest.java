package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotCreateException;
import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService 클래스")
class TaskServiceTest {
    private TaskService taskService;
    private static final String TASK_TITLE = "test";
    private static final Long NOT_EXIST_ID = 999999999L;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {
        @Nested
        @DisplayName("등록된 할 일이 없다면")
        class Context_no_have_tasks {
            @Test
            @DisplayName("비어있는 리스트를 리턴합니다")
            void it_returns_emptyTasks() {
                final List<Task> tasks = taskService.getTasks();
                assertThat(tasks).isEmpty();
            }
        }

        @Nested
        @DisplayName("등록된 할 일이 있다면")
        class Context_have_tasks {
            Task task1, task2;

            @BeforeEach
            void prepareTasks() {
                task1 = new Task();
                task1.setTitle("test");
                task2 = new Task();
                task2.setTitle("test");
                taskService.createTask(task1);
                taskService.createTask(task2);
            }

            @Test
            @DisplayName("할 일들을 리턴합니다")
            void it_returns_tasks() {
                final List<Task> tasks = taskService.getTasks();

                assertThat(tasks.size()).isEqualTo(2);
                assertThat(tasks).contains(task1, task2);
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {
        @Nested
        @DisplayName("입력받은 id와 일치하는 등록된 할 일이 없다면")
        class Context_matchId_NotExist {

            @BeforeEach
            void prepareEmptyTasks() {
                List<Task> tasks = taskService.getTasks();
                tasks.clear();
            }

            @Test
            @DisplayName("할 일을 찾을 수 없다는 예외를 던집니다")
            void it_returns_TaskNotFoundException() {
                assertThatThrownBy(() -> taskService.getTask(NOT_EXIST_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("입력받은 id와 일치하는 등록된 할 일이 있다면")
        class Context_matchId_exist {
            Task task;
            Long id;

            @BeforeEach
            void prepareTask() {
                task = new Task();
                task.setTitle("test");
                Task createdTask = taskService.createTask(this.task);
                id = createdTask.getId();
            }

            @Test
            @DisplayName("할 일을 리턴합니다")
            void it_returns_task() {
                Task foundTask = taskService.getTask(1L);

                assertThat(foundTask).isEqualTo(task);
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_createTask {
        @Nested
        @DisplayName("제목이 입력되지 않았다면")
        class Context_task_no_have_title {
            Task task;

            @BeforeEach
            void prepareTask() {
                task = new Task();
                task.setTitle(null);
            }

            @Test
            @DisplayName("할 일을 생성 할 수 없다는 예외를 던집니다")
            void createTask() {
                assertThatThrownBy(() -> taskService.createTask(task))
                        .isInstanceOf(TaskNotCreateException.class);
            }
        }

        @Nested
        @DisplayName("제목이 입력되었다면")
        class Context_task_have_title {
            Task task;

            @BeforeEach
            void prepareTask() {
                task = new Task();
                task.setTitle("test");
            }

            @Test
            @DisplayName("할 일을 생성하여 리턴합니다")
            void createTask() {
                Task createdTask = taskService.createTask(task);

                Task foundItem = taskService.getTask(createdTask.getId());
                assertThat(foundItem).isEqualTo(createdTask);
            }
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {
        @Nested
        @DisplayName("입력받은 id와 일치하는 등록된 할 일이 있다면")
        class Context_matchId_exist {
            Task task, updateTask;
            Long itemId;

            @BeforeEach
            void prepareTask() {
                task = new Task();
                task.setTitle(TASK_TITLE);

                Task createdTask = taskService.createTask(task);
                itemId = createdTask.getId();

                updateTask = new Task();
                updateTask.setTitle("update title");
            }

            @Test
            @DisplayName("입력받은 제목으로 수정된 할 일을 리턴 합니다")
            void updateTask() {
                taskService.updateTask(itemId, updateTask);

                Task foundItem = taskService.getTask(itemId);
                assertThat(foundItem.getTitle()).isEqualTo(updateTask.getTitle());
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_deleteTask {
        @Nested
        @DisplayName("입력받은 id와 일치하는 등록된 할 일이 있다면")
        class Context_matchId_exist {
            Task task, createdTask;

            @BeforeEach
            void prepareTask() {
                task = new Task();
                task.setTitle("test");
                createdTask = taskService.createTask(task);
            }

            @Test
            @DisplayName("할 일을 삭제합니다")
            void deleteTask() {
                taskService.deleteTask(createdTask.getId());

                final List<Task> tasks = taskService.getTasks();
                assertThat(tasks).doesNotContain(task);
            }
        }
    }
}
