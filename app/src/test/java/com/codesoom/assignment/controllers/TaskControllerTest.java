package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskController 클래스의")
class TaskControllerTest {
    class FakeTaskService implements TaskService {
        private Long id = 0L;
        private final Map<Long, Task> taskMap = new HashMap<>();

        @Override
        public Collection<Task> getTasks() {
            return taskMap.values();
        }

        @Override
        public Task getTask(Long id) {
            return Optional.ofNullable(taskMap.get(id))
                    .orElseThrow(() -> new TaskNotFoundException(id));
        }

        @Override
        public Task createTask(String title) {
            Task task = new Task(id, title);
            taskMap.put(id++, task);
            return task;
        }

        @Override
        public Task updateTask(Long id, String title) {
            return getTask(id).changeTitle(title);
        }

        @Override
        public void deleteTask(Long id) {
            taskMap.remove(id);
        }
    }
    private TaskController taskController;
    private TaskService taskService;
    private final Long givenId1 = 0L;
    private final String givenTodo1 = "Todo0";
    private final String givenTodo2 = "Todo1";
    private final String givenTitleToChange = "변경 후";

    @BeforeEach
    void setUp() {
        taskService = new FakeTaskService();
        taskController = new TaskController(taskService);
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {
        @Nested
        @DisplayName("빈 작업 목록이 주어졌을 때")
        class Context_with_empty_list {
            @Test
            @DisplayName("빈 배열을 리턴한다")
            void It_return_empty_list_and_status_ok() {
                assertThat(taskController.list()).isEmpty();
            }
        }

        @Nested
        @DisplayName("작업을 가진 목록이 주어지면")
        class Context_with_list {
            void prepare() {
                taskService.createTask(givenTodo1);
                taskService.createTask(givenTodo2);
            }

            @Test
            @DisplayName("작업 목록을 보낸다")
            void It_returns_list_and_ok() {
                prepare();

                assertThat(taskController.list()).hasSize(2);
            }
        }
    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detail {
        @Nested
        @DisplayName("식별자가 주어지고 해당 식별자를 가진 작업이 있다면")
        class Context_with_id_and_task {
            void prepare() {
                taskService.createTask(givenTodo1);
            }

            @Test
            @DisplayName("작업을 리턴한다")
            void It_returns_task() {
                prepare();

                assertThat(taskController.detail(givenId1))
                        .isEqualTo(new Task(givenId1, givenTodo1));
            }
        }

        @Nested
        @DisplayName("작업을 찾지 못했다면")
        class Context_without_task {
            void prepare() {
                taskService.createTask(givenTodo1);
                taskService.deleteTask(givenId1);
            }

            @Test
            @DisplayName("에러 응답과 리턴합니다")
            void It_returns_error_response_and_not_found() {
                prepare();

                assertThatThrownBy(() -> taskController.detail(givenId1))
                        .isExactlyInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {
        @Nested
        @DisplayName("제목이 주어지면")
        class Context_with_title {
            @Test
            @DisplayName("식별자를 가진 작업을 리턴한다")
            void It_returns_task_with_id() {
                assertThat(taskController.create(new Task(null, givenTodo1)))
                        .isEqualTo(new Task(givenId1, givenTodo1));
            }
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {
        @Nested
        @DisplayName("주어진 식별자를 갖는 작업이 있을 때")
        class Context_when_exists_task {
            void prepare() {
                taskService.createTask(givenTodo1);
            }

            @Test
            @DisplayName("작업의 제목을 변경하고 리턴한다")
            void It_change_title_and_return() {
                prepare();
                ;
                assertThat(taskController.update(givenId1, new Task(null, givenTitleToChange)))
                        .isEqualTo(new Task(givenId1, givenTitleToChange));
            }
        }
    }

    @Nested
    @DisplayName("patch 메소드는")
    class Describe_patch {
        @Nested
        @DisplayName("주어진 식별자를 갖는 작업이 있을 때")
        class Context_when_exists_task {
            void prepare() {
                taskService.createTask(givenTodo1);
            }

            @Test
            @DisplayName("작업의 제목을 변경하고 리턴한다")
            void It_change_title_and_return() {
                prepare();

                assertThat(taskController.update(givenId1, new Task(null, givenTitleToChange)))
                        .isEqualTo(new Task(givenId1, givenTitleToChange));
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {
        @Nested
        @DisplayName("주어진 식별자를 가진 작업이 있으면")
        class Context_with_task {
            void prepare() {
                taskService.createTask(givenTodo1);
            }

            @Test
            @DisplayName("작업을 삭제한다")
            void It_delete_task() {
                prepare();

                taskController.delete(givenId1);

                assertThat(taskController.list()).isEmpty();
            }
        }
    }
}
