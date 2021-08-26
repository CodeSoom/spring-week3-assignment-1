package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TaskService 클래스")
class TaskServiceTest {

    private final String[] TASK_TITLE= {"","test1", "test2", "test3", "test4", "test5"};
    private final String TASK_UPDATE = "update";

    TaskService taskService = new TaskService();


    @BeforeEach
    @DisplayName("Task 객체 초기화")
    void setUp() {

        Task task1 = new Task();
        task1.setTitle(TASK_TITLE[1]);

        Task task2 = new Task();
        task2.setTitle(TASK_TITLE[2]);

        Task task3 = new Task();
        task2.setTitle(TASK_TITLE[3]);

        taskService.createTask(task1);
        taskService.createTask(task2);
        taskService.createTask(task3);

    }


    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {

        @Nested
        @DisplayName("리스트에 값이 들어있다면")
        class Context_exist_task_list {

            @Test
            @DisplayName("Task 객체들을 리턴한다")
            void It_return_task_list() {

                Assertions.assertThat(taskService.getTasks()).hasSize(3);

            }

        }

    }


    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {

        @Nested
        @DisplayName("검색한 아이디가 존재하면")
        class Context_exist_id {

            @Test
            @DisplayName("Task 객체를 리턴한다")
            void It_return_task_detail() {

                assertEquals(TASK_TITLE[1], taskService.getTask(1L).getTitle());

            }

        }

        @Nested
        @DisplayName("검색한 아이디가 존재하지 않는다면")
        class Context_exist_not_id {

            @Test
            @DisplayName("TaskNotFoundException 예외를 던져준다")
            void It_return_task_throw() {

                assertThatThrownBy(() -> {
                    taskService.getTask(100L);})
                        .isInstanceOf(TaskNotFoundException.class);

            }

        }

    }


    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {

        @Nested
        @DisplayName("Task 객체를 생성하여 create 메소드를 호출해주면")
        class Context_create_task {

            @Test
            @DisplayName("아이디를 자동으로 생성하고 List에 넣어준다")
            void It_task_create() {

                Task task4 = new Task();
                task4.setTitle(TASK_TITLE[4]);
                taskService.createTask(task4);

                assertEquals(4L, taskService.getTask(4L).getId());
                assertEquals(TASK_TITLE[4], taskService.getTask(4L).getTitle());

            }

        }

    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {

        @Nested
        @DisplayName("수정할 객체의 아이디와 수정할 값을 입력해주면")
        class Context_task_update {

            @Test
            @DisplayName("Task 객체를 찾아서 값을 수정해준다")
            void It_task_update() {

                Task updateTask = new Task();
                updateTask.setTitle(TASK_TITLE[1]+TASK_UPDATE);

                taskService.updateTask(1L, updateTask);

                assertEquals(TASK_TITLE[1]+TASK_UPDATE, taskService.getTask(1L).getTitle());

            }

        }

    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {

        @Nested
        @DisplayName("삭제하고 싶은 아이디를 입력해주면")
        class Context_delete_id {

            @Test
            @DisplayName("Task 객체를 찾아 List에서 삭제한다")
            void It_task_delete() {

                taskService.deleteTask(1L);
                Assertions.assertThat(taskService.getTasks()).hasSize(2);

            }

        }

    }

}
