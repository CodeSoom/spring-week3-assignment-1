package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("TaskService 클래스")
class TaskServiceTest {

    private final String[] TASK_TITLE = {"test1", "test2", "test3", "test4", "test5"};
    private final String TASK_UPDATE = "update";
    private final Long VALID_ID = 1L;
    private final Long INVALID_ID = 100L;

    TaskService taskService = new TaskService();


    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {

        @Nested
        @DisplayName("리스트에 값이 들어있다면")
        class Context_exist_task_list {

            @BeforeEach
            @DisplayName("tasks 리스트를 초기화합니다")
            void tasks_setUp() {

                for (int i = 0; i < TASK_TITLE.length; i++) {
                    Task task = new Task();
                    task.setTitle(TASK_TITLE[i]);
                    taskService.createTask(task);
                }

            }

            @Test
            @DisplayName("Task 객체들을 리턴한다")
            void It_return_task_list() {

                Assertions.assertThat(taskService.getTasks()).hasSize(5);

            }

        }

    }


    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {

        @Nested
        @DisplayName("검색한 아이디가 존재하면")
        class Context_exist_id {

            @BeforeEach
            @DisplayName("검색할 Task 객체를 세팅합니다")
            void task_setUp() {

                Task task = new Task();
                task.setTitle(TASK_TITLE[0]);
                taskService.createTask(task);

            }

            @Test
            @DisplayName("Task 객체를 리턴한다")
            void It_return_task_detail() {

                assertEquals(VALID_ID, taskService.getTask(VALID_ID).getId());
                assertEquals(TASK_TITLE[0], taskService.getTask(VALID_ID).getTitle());

            }

        }

        @Nested
        @DisplayName("검색한 아이디가 존재하지 않는다면")
        class Context_exist_not_id {

            @Test
            @DisplayName("TaskNotFoundException 예외를 던져준다")
            void It_return_task_throw() {

                assertThatThrownBy(() -> {
                    taskService.getTask(INVALID_ID);})
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

            Task task = new Task();

            @BeforeEach
            @DisplayName("추가할 Task 객체를 세팅합니다")
            void create_setUp() {

                task.setTitle(TASK_TITLE[0]);

            }

            @Test
            @DisplayName("아이디를 자동으로 생성하고 List에 넣어준다")
            void It_task_create() {

                taskService.createTask(task);

                assertEquals(VALID_ID, taskService.getTask(VALID_ID).getId());
                assertEquals(TASK_TITLE[0], taskService.getTask(VALID_ID).getTitle());

            }

        }

    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {

        @Nested
        @DisplayName("수정할 객체의 아이디와 수정할 값이 주어지면")
        class Context_task_update {

            Task updateTask = new Task();

            @BeforeEach
            @DisplayName("수정할 Task 객체를 세팅합니다.")
            void update_setUp() {

                Task task = new Task();
                task.setTitle(TASK_TITLE[0]);
                taskService.createTask(task);

                updateTask.setTitle(TASK_TITLE[0] + TASK_UPDATE);

            }

            @Test
            @DisplayName("Task 객체를 찾아서 값을 수정해준다")
            void It_task_update() {

                taskService.updateTask(VALID_ID, updateTask);

                assertEquals(TASK_TITLE[0]+TASK_UPDATE, taskService.getTask(VALID_ID).getTitle());

            }

        }

    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {

        @Nested
        @DisplayName("삭제하고 싶은 아이디가 주어지면")
        class Context_delete_id {

            Long deleteId = VALID_ID;

            @BeforeEach
            @DisplayName("삭제할 Task 객체를 세팅합니다")
            void delete_setUp() {

                Task task = new Task();
                task.setTitle(TASK_TITLE[0]);
                taskService.createTask(task);

            }


            @Test
            @DisplayName("Task 객체를 찾아 List에서 삭제한다")
            void It_task_delete() {

                taskService.deleteTask(deleteId);

                assertThatThrownBy(() -> {
                    taskService.getTask(deleteId);
                }).isInstanceOf(TaskNotFoundException.class);

                Assertions.assertThat(taskService.getTasks()).isEmpty();

            }

        }

    }

}
