package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("TaskController 테스트")
class TaskControllerTest {
    private TaskService taskService;
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);

        Task createTask = new Task();
        createTask.setTitle("Test1");
        taskController.create(createTask);
    }

    @Nested
    @DisplayName("list 메서드는")
    class Describe_list {
        @Test
        @DisplayName("Task 리스트를 반환한다")
        void itReturnsListOfTask() {
            Assertions.assertEquals(taskController.list().size(), 1, "리턴된 Task 리스트는 사이즈 값으로 1을 갖는다");
        }
    }

    @Nested
    @DisplayName("detail 메서드는")
    class Describe_detail {
        @Nested
        @DisplayName("만약 Url에 유효한 id가 주어진다면")
        class ContextWithValidUrlId {
            @Test
            @DisplayName("유효한 id에 해당하는 Task를 리턴한다")
            void itReturnsValidTask() {
                Assertions.assertEquals(taskController.list().get(0).getId(), 1L, "리턴된 Task는 id 값으로 1L을 갖는다");
                Assertions.assertEquals(taskController.list().get(0).getTitle(), "Test1", "리턴된 Task는 title 값으로 Test1을 갖는다");
            }
        }
    }

    @Nested
    @DisplayName("create 메서드는")
    class Describe_create {
        @Test
        @DisplayName("title을 입력받아 새로운 Task를 생성한다")
        void itReturnsNewTask () {
            Task createTask = new Task();
            createTask.setTitle("Test2");
            taskController.create(createTask);

            Assertions.assertEquals(taskController.list().size(), 2,"새롭게 Task를 생성한 후 Task 리스트는 사이즈 값으로 2를 갖는다");
            Assertions.assertEquals(taskController.list().get(1).getId(), 2L,"새로 생성되어 리턴한 Task는 id 값으로 2L을 갖는다");
            Assertions.assertEquals(taskController.list().get(1).getTitle(), "Test2","새로 생성되어 리턴한 Task는 title 값으로 Test2를 갖는다");
        }

    }

    @Nested
    @DisplayName("update 메서드는")
    class Describe_update {
        @Nested
        @DisplayName("만약 Url에 유효한 id가 주어진다면")
        class ContextWithValidUrlId {
            @Test
            @DisplayName("유효한 id에 해당하는 Task의 title을 수정하고 Task를 리턴한다")
            void itReturnsValidUpdatedTask() {
                Task updateTask = new Task();
                updateTask.setTitle("new Task");
                taskController.update(1L, updateTask);

                Assertions.assertEquals(taskController.list().get(0).getId(), 1L, "업데이트 되어 리턴된 Task는 id값으로 1L을 갖는다");
                Assertions.assertEquals(taskController.list().get(0).getTitle(), "new Task", "업데이트 되어 리턴된 Task는 title값으로 new Task를 갖는다");
            }
        }
    }

    @Nested
    @DisplayName("patch 메서드는")
    class Describe_patch {
        @Nested
        @DisplayName("만약 Url에 유효한 id가 주어진다면")
        class ContextWithValidUrlId {
            @Test
            @DisplayName("유유효한 id에 해당하는 Task의 title을 수정하고 Task를 리턴한다")
            void itReturnsValidUpdatedTask() {
                Task updateTask = new Task();
                updateTask.setTitle("new Task");
                taskController.patch(1L, updateTask);

                Assertions.assertEquals(taskController.list().get(0).getId(), 1L, "업데이트 되어 리턴된 Task는 id값으로 1L을 갖는다");
                Assertions.assertEquals(taskController.list().get(0).getTitle(), "new Task", "업데이트 되어 리턴된 Task는 title값으로 new Task를 갖는다");
            }
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class Describe_delete {
        @Nested
        @DisplayName("만약 Url에 유효한 id가 주어진다면")
        class ContextWithValidUrlId {
            @Test
            @DisplayName("유효한 id에 해당하는 Task를 삭제하고 빈 문자열을 리턴한다")
            void itReturnsEmptyString() {
                taskController.delete(1L);

                Assertions.assertEquals(taskController.list().toString(), "[]");
            }
        }
    }
}
