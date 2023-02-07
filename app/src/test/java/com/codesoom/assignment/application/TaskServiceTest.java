package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    private TaskService taskService;

    private static final String TASK_TITLE = "Test1";

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);
    }

    @Test
    @DisplayName("list 메소드는 등록되어 있는 메소드를 호출한다.")
    void getList() {
        assertThat(taskService.getTasks()).hasSize(1);
    }


    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detailTask {
        private final Long EXISTING_TASK_ID = 1L;
        private final Long NOT_EXISTING_TASK_ID = 404L;
        private Long taskId;

        @Nested
        @DisplayName("만약 등록된 할 일 1개를 조회 요청을 한다면")
        class Context_detailVaildTask {

            @BeforeEach
            void setUp() {
                taskId = EXISTING_TASK_ID;
            }

            @Test
            @DisplayName("요청에 맞는 할 일을 응답한다.")
            void getFoundDetail() {
                assertThat(taskService.getTask(taskId).getTitle()).isEqualTo(TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("만약 등록되지 않은 할 일 1개를 조회 요청을 한다면")
        class Context_detailInvaildTask {

            @BeforeEach
            void setUp() {
                taskId = NOT_EXISTING_TASK_ID;
            }

            @Test
            @DisplayName("할 일을 찾을 수 없다는 예외를 던진다.")
            void getNotFoundDetail() {
                assertThatThrownBy(() -> taskService.getTask(404L)).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_createTask {

        @Nested
        @DisplayName("새로운 할 일 요청을 한다면")
        class Context_createVaildTask {
            private final String createTitle = "Test2";

            @Test
            @DisplayName("새로운 Task를 생성한다.")
            void createTask() {
                int oldSize = taskService.getTasks().size();

                Task source = new Task();
                source.setTitle(createTitle);

                taskService.createTask(source);

                int newSize = taskService.getTasks().size();

                assertThat(newSize - oldSize).isEqualTo(1);
            }
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {
        private final Long EXISTING_TASK_ID = 1L;
        private final Long NOT_EXISTING_TASK_ID = 404L;
        private Long taskId;

        private String updateTitle = "Test2";
        private Task source;

        @BeforeEach
        void setUp() {
            source = new Task();
            source.setTitle(updateTitle);
        }

        @Nested
        @DisplayName("만약 등록되어 있는 Id와 새로운 제목이 주어진다면")
        class Context_updateValidTask {

            @BeforeEach
            void setUp() {
                taskId = EXISTING_TASK_ID;
            }

            @Test
            @DisplayName("등록 된 Id를 새로운 제목으로 업데이트 한다")
            void updateTask() {

                Task task = taskService.updateTask(taskId, source);

                assertThat(task.getTitle()).isEqualTo(updateTitle);
            }
        }

        @Nested
        @DisplayName("만약 등록되어 있지 않은 Id와 새로운 제목이 주어진다면")
        class Context_updateInvalidTask {

            @BeforeEach
            void setUp() {
                taskId = NOT_EXISTING_TASK_ID;
            }

            @Test
            @DisplayName("찾을 수 없다는 예외를 던진다")
            void updateNotFoundTask() {
                assertThatThrownBy(() -> taskService.updateTask(taskId, source))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_deleteTast {
        private final Long EXISTING_TASK_ID = 1L;
        private final Long NOT_EXISTING_TASK_ID = 404L;
        private Long taskId;

        @Nested
        @DisplayName("만약 등록되어 있는 Id가 주어진다면")
        class Context_deleteValidTask {

            @BeforeEach
            void setUp() {
                taskId = EXISTING_TASK_ID;
            }

            @Test
            @DisplayName("등록 된 Id를 삭제 처리 한다.")
            void deleteTask() {

                int oldSize = taskService.getTasks().size();

                taskService.deleteTask(1L);

                int newSize = taskService.getTasks().size();

                assertThat(newSize - oldSize).isEqualTo(0);
            }
        }

        @Nested
        @DisplayName("만약 등록되어 있지 않은 Id가 주어진다면")
        class Context_deleteInvalidTask {

            @BeforeEach
            void setUp() {
                taskId = NOT_EXISTING_TASK_ID;
            }

            @Test
            @DisplayName("삭제를 할 수 없다는 예외를 던진다")
            void deleteTask() {
                assertThatThrownBy(() -> taskService.deleteTask(taskId))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

}
