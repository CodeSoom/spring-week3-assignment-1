package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class TaskControllerTest {

    @Autowired
    TaskController taskController;

    @AfterEach
    void after() {
        List<Task> taskList = taskController.getTasks();
        taskList.clear();
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {
        @Nested
        @DisplayName("아무 값이 없다면")
        class Context_when_noTask {

            @Test
            @DisplayName("빈 배열을 리턴한다")
            void it_returns_empty_list() {
                assertThat(taskController.getTasks()).isEmpty();
            }
        }

        @Nested
        @DisplayName("만약 Task가 있다면")
        class Context_when_hasTask {
            @Test
            @DisplayName("Task 객체를 리턴한다")
            void it_returns_task() {
                assertThat(createTask()).isInstanceOf(Task.class);
                assertThat(taskController.getTasks()).isNotEmpty();
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {
        @Nested
        @DisplayName("존재하는 id로 검색한다면")
        class Context_with_validId {
            @Test
            @DisplayName("id에 해당하는 Task를 리턴한다")
            void it_returns_task() {
                Long taskId = createTask().getId();
                assertThat(taskController.getTask(taskId)).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_createTask {
        @Nested
        @DisplayName("requestBody에서 정상적으로 title이 넘어온다면")
        class Context_with_title {
            @Test
            @DisplayName("Task를 생성한다")
            void it_returns_task() {
                int oldSize = taskController.getTasks().size();
                createTask();
                int newSize = taskController.getTasks().size();

                assertThat(newSize - oldSize).isEqualTo(1);
            }
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_updateTask {
        Task setNewTitle(String title) {
            Task source = new Task();
            source.setTitle(title);

            return taskController.createTask(source);
        }

        @Nested
        @DisplayName("PUT 호출할 때")
        class Context_when_PUT {
            @Test
            @DisplayName("title을 업데이트한다")
            void it_updates_title() {
                Task source = setNewTitle("청소하기");
                Task task = taskController.updateTask(source.getId(), source);

                assertThat(task.getTitle()).isEqualTo("청소하기");
            }
        }

        @Nested
        @DisplayName("PATCH 호출할 때")
        class Context_when_PATCH {
            @Test
            @DisplayName("title을 업데이트한다")
            void it_updates_title() {
                Task source = setNewTitle("잠 자기");
                Task task = taskController.updateTask(source.getId(), source);

                assertThat(task.getTitle()).isEqualTo("잠 자기");
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_deleteTask {
        @Nested
        @DisplayName("DELETE 호출할 때")
        class Context_with_validId {
            @Test
            @DisplayName("삭제된 id로 호출하면 TaskNotFoundException을 던진다")
            void it_returns_TaskNotFoundException() {
                Long taskId = createTask().getId();
                taskController.deleteTask(taskId);

                assertThatThrownBy(() -> taskController.getTask(taskId))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    Task createTask() {
        Task task = new Task();
        task.setTitle("공부하기");

        return taskController.createTask(task);
    }
}
