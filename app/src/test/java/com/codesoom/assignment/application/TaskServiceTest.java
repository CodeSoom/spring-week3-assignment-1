package com.codesoom.assignment.application;

import com.codesoom.assignment.BaseTaskTest;
import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName(value = "TaskControllerMockTest 에서")
class TaskServiceTest extends BaseTaskTest {

    private TaskService taskService;

    @BeforeEach()
    void setUp() {
        taskService = new TaskService();
    }


    @Test
    @DisplayName("신규 할일 번호 자동 생성")
    void generateNewTaskId() {
        Task firstTask = generateNewTask(TASK_TITLE_1);
        Task secondTask = generateNewTask(TASK_TITLE_2);

        Task firstCreatedTask = taskService.createTask(firstTask);
        Task secondCreatedTask = taskService.createTask(secondTask);

        Long idGap = secondCreatedTask.getId() - firstCreatedTask.getId();
        assertThat(idGap).isEqualTo(1L);
    }


    @Nested
    @DisplayName(value = "getTasks() 매소드는 ")
    class Describe_getTasks {

        @Nested
        @DisplayName("할일목록이 없다면")
        class Context_with_empty_tasks {

            @Test
            @DisplayName("사이즈가 0인 할일리스트를 반환한다.")
            void it_returns_empty_list() {

                List<Task> tasks = taskService.getTasks();

                assertThat(tasks).hasSize(0);
            }
        }

        @Nested
        @DisplayName("할일목록에 할일이 있다면")
        class Context_with_tasks {

            @Test
            @DisplayName("사이즈가 0이 아닌 할일 리스트를 반환한다.")
            void it_returns_task_list_gt_0() {

                Task newTask = generateNewTask(TASK_TITLE_1);
                taskService.createTask(newTask);

                List<Task> tasks = taskService.getTasks();
                assertThat(tasks).hasSize(1);
            }
        }
    }

    @Nested
    @DisplayName("getTask() 매소드는")
    class Describe_getTask {

        @Nested
        @DisplayName("id와 일치하는 값이 있다면")
        class Context_with_matched_task {

            @Test
            @DisplayName("task 내용을 반환한다.")
            void it_returns_single_task() {

                Task newTask = generateNewTask(TASK_TITLE_1);
                Task created = taskService.createTask(newTask);
                Task found = taskService.getTask(1L);

                assertThat(found).isNotNull();
                assertThat(found).isEqualTo(created);
            }
        }

        @Nested
        @DisplayName("id와 일치하는 값이 없다면")
        class Context_without_matched_task {
            @Test
            @DisplayName("Exception 을 반환한다.")
            void it_throws_not_found_exception() {

                assertThatThrownBy(() -> taskService.getTask(123L))
                        .isInstanceOf(TaskNotFoundException.class)
                        .hasMessageContaining(ERROR_MSG_TASK_NOT_FOUND);
            }
        }
    }

    @Nested
    @DisplayName("addTask() 매소드는")
    class Describe_addTask {

        @Nested
        @DisplayName("할일제목이 없거나 공백이 아닌 Task 값이 입력되면")
        class Context_normal_task {

            @Test
            @DisplayName("할일 목록에 신규할일을 추가하고, 추가된 할일을 반환한다.")
            void it_adds_new_task_and_returns_added_task() {

                Task newTask = generateNewTask(TASK_TITLE_1);

                Task created = taskService.createTask(newTask);

                assertThat(created.getId()).isEqualTo(TASK_ID_1);
                assertThat(created.getTitle()).isEqualTo(TASK_TITLE_1);
            }
        }
    }



    @Nested
    @DisplayName("updateTask() 매소드는")
    class Describe_updateTask {

        @Test
        @DisplayName("path id 와 일치하는 task 가 존재하면 > 제목을 수정한 후 > 수정된 task 를 반환한다.")
        void it_returns_edited_task() {

            Task newTask = generateNewTask(TASK_TITLE_1);
            taskService.createTask(newTask);

            Task editedTask = generateNewTask(TASK_TITLE_2);
            Task updatedTask = taskService.updateTask(TASK_ID_1, editedTask);

            assertThat(updatedTask.getTitle()).isNotEqualTo(TASK_TITLE_1);
            assertThat(updatedTask.getTitle()).isEqualTo(TASK_TITLE_2);
        }

        @Test
        @DisplayName("path id 와 일치하는 task 가 존재하지 않으면 > 오류를 반환한다.")
        void it_throws_exception() {

            assertThatThrownBy(() -> {
                Task editedTask = generateNewTask(TASK_TITLE_2);
                taskService.updateTask(123L, editedTask);
            }).isInstanceOf(TaskNotFoundException.class)
                    .hasMessageContaining(ERROR_MSG_TASK_NOT_FOUND);
        }

    }


    @Nested
    @DisplayName("deleteTask() 매소드는")
    class Describe_deleteTask {

        @Nested
        @DisplayName("path id 와 일치하는 task를 찾을 수 있을때")
        class Context_has_matched_task {

            @Test
            @DisplayName("id에 맞는 할일 조회 후 삭제한다.")
            void it_deletes_task() {
                Task newTask = generateNewTask(TASK_TITLE_1);
                Task created = taskService.createTask(newTask);

                Task deleted = taskService.deleteTask(created.getId());

                List<Task> tasks = taskService.getTasks();

                assertThat(tasks).doesNotContain(deleted);
            }
        }

        @Nested
        @DisplayName("path id 와 일치하는 task를 찾을 수 없을때")
        class Context_no_matched_task {

            @Test
            @DisplayName("예외를 던진다.")
            void it_throws_exception() {

                assertThatThrownBy(() -> {
                    taskService.deleteTask(TASK_ID_1);
                }).isInstanceOf(TaskNotFoundException.class)
                        .hasMessageContaining(ERROR_MSG_TASK_NOT_FOUND);
            }
        }
    }

}
