package com.codesoom.assignment.controllers;

import com.codesoom.assignment.BaseTaskTest;
import com.codesoom.assignment.NotProperTaskFormatException;
import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName(value = "TaskControllerTest 에서")
class TaskControllerTest extends BaseTaskTest {

    private TaskController taskController;

    @BeforeEach
    void setUp() {
        TaskService taskService = new TaskService();
        taskController = new TaskController(taskService);
    }

    @Nested
    @DisplayName(value = "list() 매소드는 ")
    class Describe_readTasks {

        @Nested
        @DisplayName("할일목록이 없다면")
        class Context_with_empty_tasks {

            @Test
            @DisplayName("사이즈가 0인 할일 리스트를 반환한다.")
            void it_returns_empty_list() {
                List<Task> tasks = taskController.list();

                assertThat(tasks).hasSize(0);
            }
        }

        @Nested
        @DisplayName("할일목록에 할일이 있다면")
        class Context_with_tasks {

            @Test
            @DisplayName("사이즈가 0이 아닌 할일목록을 반환한다.")
            void it_returns_full_list() {
                taskController.create(generateNewTask(TASK_TITLE_1));

                List<Task> tasks = taskController.list();
                assertThat(tasks).hasSize(1);
            }
        }
    }

    @Nested
    @DisplayName("readTask() 매소드는")
    class Describe_readTask {

        @Nested
        @DisplayName("path id 가 1이상일때")
        class Context_with_valid_path {

            @Nested
            @DisplayName("id와 일치하는 값이 있다면")
            class Context_with_matched_task {

                @Test
                @DisplayName("task 내용을 반환한다.")
                void it_returns_single_task() {

                    Task created = taskController.create(generateNewTask(TASK_TITLE_1));
                    Task found = taskController.detail(1L);

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

                    assertThatThrownBy(() -> taskController.detail(1L))
                            .isInstanceOf(TaskNotFoundException.class)
                            .hasMessageContaining(ERROR_MSG_TASK_NOT_FOUND);
                }
            }
        }

        @Nested
        @DisplayName("path id가 0이하 일때")
        class Context_with_invalid_path {

            @Test
            @DisplayName("예외를 발생시킨다.")
            void it_throws_exception() {
                assertThatThrownBy(() -> taskController.detail(0L))
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

                Task created = taskController.create(generateNewTask(TASK_TITLE_1));

                assertThat(created.getId()).isEqualTo(TASK_ID_1);
                assertThat(created.getTitle()).isEqualTo(TASK_TITLE_1);
            }
        }

        @Nested
        @DisplayName("할일제목이 없거나 공백인 Task 값이 입력되면")
        class Context_abnormal_task {

            @Test
            @DisplayName("예외를 발생시킨다.")
            void it_throws_exception() {

                assertThatThrownBy(() -> taskController.create(generateNewTask("")))
                        .isInstanceOf(NotProperTaskFormatException.class)
                        .hasMessageContaining(ERROR_MSG_TASK_FORMAT_BAD);
            }
        }
    }


    @Nested
    @DisplayName("editTasks() 매소드는")
    class Describe_editTask {

        @Nested
        @DisplayName("path id가 0이상 이라면")
        class Context_normal_path_id {

            @Nested
            @DisplayName("할일제목이 없거나 공백이 아닌 Task 값이 입력되면")
            class Context_normal_task {

                @Test
                @DisplayName("path id 와 일치하는 task 가 존재하면 > 제목을 수정한 후 > 수정된 task 를 반환한다.")
                void it_returns_edited_task() {

                    taskController.create(generateNewTask(TASK_TITLE_1));
                    Task updatedTask = taskController.update(TASK_ID_1, generateNewTask(TASK_TITLE_2));

                    assertThat(updatedTask.getTitle()).isNotEqualTo(TASK_TITLE_1);
                    assertThat(updatedTask.getTitle()).isEqualTo(TASK_TITLE_2);
                }

                @Test
                @DisplayName("path id와 일치하는 task 가 존재하지 않으면 > 오류를 반환한다.")
                void it_throws_exception() {
                    assertThatThrownBy(() -> {
                        taskController.update(TASK_ID_1, generateNewTask(TASK_TITLE_2));
                    }).isInstanceOf(TaskNotFoundException.class)
                            .hasMessageContaining(ERROR_MSG_TASK_NOT_FOUND);
                }
            }

            @Nested
            @DisplayName("할일제목이 없거나 공백인 Task 값이 입력되면")
            class Context_abnormal_task {

                @Test
                @DisplayName("예외를 반환한다.")
                void editTaskTitle_throwErrorIfNotValidTitle() {
                    taskController.create(generateNewTask(TASK_TITLE_1));

                    assertThatThrownBy(() -> {
                        taskController.update(TASK_ID_1, generateNewTask(""));
                    }).isInstanceOf(NotProperTaskFormatException.class)
                            .hasMessageContaining(ERROR_MSG_TASK_FORMAT_BAD);
                }
            }

        }

        @Nested
        @DisplayName("path id가 0이하 이라면")
        class Context_abnormal_path_id {

            @Test
            @DisplayName("예외를 던진다.")
            void it_throws_exception() {
                taskController.create(generateNewTask(TASK_TITLE_1));

                assertThatThrownBy(() -> {
                    taskController.update(0L, generateNewTask(TASK_TITLE_2));
                }).isInstanceOf(TaskNotFoundException.class)
                        .hasMessageContaining(ERROR_MSG_TASK_NOT_FOUND);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask() 매소드는")
    class Describe_deleteTask {

        @Nested
        @DisplayName("path id가 0이하 라면")
        class Context_valid_path_id {

            @Nested
            @DisplayName("path id 와 일치하는 task를 찾을 수 있을때")
            class Context_has_matched_task {

                @Test
                @DisplayName("id에 맞는 할일 조회 후 삭제한다.")
                void it_deletes_task() {
                    Task created = taskController.create(generateNewTask(TASK_TITLE_1));
                    taskController.delete(created.getId());

                    List<Task> tasks = taskController.list();

                    assertThat(tasks).hasSize(0);
                }
            }

            @Nested
            @DisplayName("path id 와 일치하는 task를 찾을 수 없을때")
            class Context_no_matched_task {

                @Test
                @DisplayName("예외를 던진다.")
                void it_throws_exception() {

                    assertThatThrownBy(() -> {
                        taskController.delete(TASK_ID_1);
                    }).isInstanceOf(TaskNotFoundException.class)
                            .hasMessageContaining(ERROR_MSG_TASK_NOT_FOUND);
                }
            }

        }

        @Nested
        @DisplayName("path id가 0이하 라면")
        class Context_invalid_path_id {

            @Test
            @DisplayName("예외를 던진다.")
            void it_throws_exception() {
                assertThatThrownBy(() -> taskController.delete(0L))
                        .isInstanceOf(TaskNotFoundException.class)
                        .hasMessageContaining(ERROR_MSG_TASK_NOT_FOUND);
            }
        }
    }
}
