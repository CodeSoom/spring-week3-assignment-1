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
        @DisplayName("할 일의 목록을 리턴한다")
        void itReturnsListOfTask() {
            Assertions.assertEquals(taskController.list().size(), 1, "리턴된 할 일 리스트의 사이즈가 1이어야 한다");
        }
    }

    @Nested
    @DisplayName("detail 메서드는")
    class Describe_detail {
        @Nested
        @DisplayName("만약 Url에 저장되어 있는 할 일의 id가 주어진다면")
        class ContextWithValidUrlId {
            @Test
            @DisplayName("주어진 id에 해당하는 할 일을 리턴한다")
            void itReturnsValidTask() {
                Assertions.assertEquals(taskController.list().get(0).getId(), 1L, "리턴된 할 일은 id 값이 1L이어야 한다");
                Assertions.assertEquals(taskController.list().get(0).getTitle(), "Test1", "리턴된 할 일은 title 값이 Test1이어야 한다");
            }
        }
    }

    @Nested
    @DisplayName("create 메서드는")
    class Describe_create {
        @Test
        @DisplayName("title을 입력받아 새로운 할 일을 생성하고 할 일을 리턴한다")
        void itReturnsNewTask () {
            Task createTask = new Task();
            createTask.setTitle("Test2");
            taskController.create(createTask);

            Assertions.assertEquals(taskController.list().size(), 2,"새롭게 할 일을 생성한 후 할 일 목록은 사이즈 값이 2이어야 한다");
            Assertions.assertEquals(taskController.list().get(1).getId(), 2L,"새로 생성되어 리턴된 할 일은 id 값이 2L이어야 한다");
            Assertions.assertEquals(taskController.list().get(1).getTitle(), "Test2","새로 생성되어 리턴된 할 일은 title 값이 Test2이어야 한다");
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Describe_update {
        @Nested
        @DisplayName("만약 Url에 저장되어 있는 할 일의 id가 주어진다면")
        class ContextWithValidUrlId {
            @Test
            @DisplayName("주어진 id에 해당하는 할 일의 title을 수정하고 할 일을 리턴한다")
            void itReturnsValidUpdatedTask() {
                Task updateTask = new Task();
                updateTask.setTitle("new Task");
                taskController.update(1L, updateTask);

                Assertions.assertEquals(taskController.list().get(0).getId(), 1L, "수정되어 되어 리턴된 할 일은 id 값이 1L이어야 한다");
                Assertions.assertEquals(taskController.list().get(0).getTitle(), "new Task", "수정되어 리턴된 할 일은 title 값이 new Task이어야 한다");
            }
        }
    }

    @Nested
    @DisplayName("patch 메서드는")
    class Describe_patch {
        @Nested
        @DisplayName("만약 Url에 저장되어 있는 할 일의 id가 주어진다면")
        class ContextWithValidUrlId {
            @Test
            @DisplayName("주어진 id에 해당하는 할 일의 title을 수정하고 할 일을 리턴한다")
            void itReturnsValidUpdatedTask() {
                Task updateTask = new Task();
                updateTask.setTitle("new Task");
                taskController.patch(1L, updateTask);

                Assertions.assertEquals(taskController.list().get(0).getId(), 1L, "수정되어 리턴된 할 일은 id 값이 1L이어야 한다");
                Assertions.assertEquals(taskController.list().get(0).getTitle(), "new Task", "수정되어 리턴된 할 일은 title 값이 new Task이어야 한다");
            }
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class Describe_delete {
        @Nested
        @DisplayName("만약 Url에 저장되어 있는 할 일의 id가 주어진다면")
        class ContextWithValidUrlId {
            @Test
            @DisplayName("주어진 id에 해당하는 할 일을 삭제하고 빈 문자열을 리턴한다")
            void itReturnsEmptyString() {
                taskController.delete(1L);

                Assertions.assertEquals(taskController.list().toString(), "[]");
            }
        }
    }
}
