package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService 클래스")
class TaskServiceTest {

    private TaskService taskService;

    private static final String TASK_TITLE = "test";
    private static final String UPDATE_POSTFIX = "!!!";

    @BeforeEach
    void setUp() {
        // subject
        taskService = new TaskService();

        // fixtures
        Task task = new Task();
        task.setTitle(TASK_TITLE);

        taskService.createTask(task);
    }

    @Nested
    @DisplayName("getTask 메소드")
    class Describe_getTask {
        @Nested
        @DisplayName("만약 id 값이 없다면")
        class Context_without_id {
            @Test
            @DisplayName("Task 전체 리스트를 가져온다.")
            void it_return_tasks() {
                // given
                List<Task> tasks = new ArrayList<>();

                // when
                tasks = taskService.getTasks();
                Task foundTask = tasks.get(0);

                // then
                assertThat(tasks).hasSize(1);
                assertThat(foundTask.getTitle()).isEqualTo(TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("만약 id 값이 있다면")
        class Context_with_id {
            @Test
            @DisplayName("task 정보를 가져온다.")
            void it_return_task() {
                // given
                Long validId = 1L;

                // when
                Task found = taskService.getTask(validId);

                // then
                assertThat(found.getTitle()).isEqualTo(TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("만약 id 값이 존재하지 않다면")
        class Context_with_invalid_id {
            @Test
            @DisplayName("TaskNotFoundException 이 발생한다.")
            void it_return_taskNotFoundException() {
                // given
                Long invalidId = 10L;

                // when & then
                assertThatThrownBy(() -> taskService.getTask(invalidId)).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드")
    class Describe_createTask {
        @Nested
        @DisplayName("만약 task 가 있다면")
        class Context_with_task {
            @Test
            @DisplayName("task 가 생성되고, 반환된다.")
            void it_create_task_return_task() {
                // given
                int oldSize = taskService.getTasks().size();
                Task task = new Task();
                task.setTitle(TASK_TITLE);

                // when
                taskService.createTask(task);
                int newSize = taskService.getTasks().size();

                // then
                assertThat(newSize - oldSize).isEqualTo(1);
            }
        }

        @Nested
        @DisplayName("만약 task 가 없다면")
        class Context_without_task {
            @Test
            @DisplayName("exception 이 발생한다.")
            void it_return_exception() {
                // given
                Task task = null;

                // when & then
                assertThatThrownBy(() -> taskService.createTask(task)).isInstanceOf(NullPointerException.class);
            }
        }
    }

    @Nested
    @DisplayName("updateTask 메소드")
    class Describe_updateTask {
        @Nested
        @DisplayName("만약 id 와 task 가 있다면")
        class Context_with_id_and_task {
            @Test
            @DisplayName("해당 id의 task가 수정되고, 반환된다.")
            void it_update_task_return_task() {
                // given
                Task source = new Task();
                source.setTitle(TASK_TITLE + UPDATE_POSTFIX);

                // when
                taskService.updateTask(1L, source);
                Task found = taskService.getTask(1L);

                // then
                assertThat(found.getTitle()).isEqualTo(TASK_TITLE + UPDATE_POSTFIX);
            }
        }

        @Nested
        @DisplayName("만약 id 만 있다면")
        class Context_with_id {
            @Test
            @DisplayName("NullPointerException 이 반환된다.")
            void it_update_task_return_task() {
                // given
                Long id = 1L;
                Task source = null;

                // when & then
                assertThatThrownBy(() -> taskService.updateTask(id, source)).isInstanceOf(NullPointerException.class);
            }
        }

        @Nested
        @DisplayName("만약 task 만 있다면")
        class Context_with_task {
            @Test
            @DisplayName("TaskNotFoundException 가 반환된다.")
            void it_return_taskNotFoundException() {
                // given
                Long id = null;
                Task source = new Task();
                source.setTitle(TASK_TITLE + UPDATE_POSTFIX);

                // when & then
                assertThatThrownBy(() -> taskService.updateTask(id, source)).isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 id와 task가 있다면 ")
        class Context_with_invalid_id_and_task {
            @Test
            @DisplayName("TaskNotFoundException 가 반환된다.")
            void it_return_taskNotFoundException() {
                // given
                Long id = 100L;
                Task source = new Task();
                source.setTitle(TASK_TITLE + UPDATE_POSTFIX);

                // when & then
                assertThatThrownBy(() -> taskService.updateTask(id, source)).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드")
    class Describe_deleteTask {
        @Nested
        @DisplayName("만약 id가 있다면")
        class Context_with_id {
            @Test
            @DisplayName("존재하는 Task ID로 Task 삭제하기")
            void it_delete_task_return() {
                // given
                int oldSize = taskService.getTasks().size();

                // when
                taskService.deleteTask(1L);
                int newSize = taskService.getTasks().size();

                // then
                assertThat(oldSize - newSize).isEqualTo(1);
            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 id가 있다면")
        class Context_with_invalid_id {
            @Test
            @DisplayName("TaskNotFoundException 이 발생한다.")
            void it_return_taskNotFoundException() {
                // given
                Long id = 10L;

                // when & then
                assertThatThrownBy(() -> taskService.deleteTask(id)).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
