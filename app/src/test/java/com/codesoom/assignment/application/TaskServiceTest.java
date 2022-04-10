package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.contexts.ContextTask;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName(value = "TaskServiceTest 에서")
class TaskServiceTest {

    @Nested
    @DisplayName("generateId() 매소드는")
    class Describe_generateId {

        @Nested
        @DisplayName("신규 할일이 추가되면")
        class Context_withNewTask extends ContextTask {

            final Task firstCreatedTask;
            final Task secondCreatedTask;

            public Context_withNewTask() {
                this.firstCreatedTask = taskService.createTask(generateNewTask(TASK_TITLE_1));
                this.secondCreatedTask = taskService.createTask(generateNewTask(TASK_TITLE_2));
            }


            @Test
            @DisplayName("신규 할일의 번호를 기존 번호에 1만큼 증가한 값으로 자동생성한다.")
            void it_automatically_generates_incremented_taskId() {
                Long idGap = secondCreatedTask.getId() - firstCreatedTask.getId();
                assertThat(idGap).isEqualTo(1L);
            }
        }
    }


    @Nested
    @DisplayName(value = "getTasks() 매소드는 ")
    class Describe_getTasks {

        @Nested
        @DisplayName("할일목록이 없다면")
        class Context_with_empty_tasks extends ContextTask {

            @Test
            @DisplayName("사이즈가 0인 할일리스트를 반환한다.")
            void it_returns_empty_list() {
                List<Task> tasks = taskService.getTasks();
                assertThat(tasks).hasSize(0);
            }
        }

        @Nested
        @DisplayName("할일목록에 할일이 있다면")
        class Context_with_tasks extends ContextTask {

            public Context_with_tasks() {
                taskService.createTask(generateNewTask(TASK_TITLE_1));
            }

            @Test
            @DisplayName("사이즈가 0이 아닌 할일 리스트를 반환한다.")
            void it_returns_task_list_gt_0() {
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
        class Context_with_matched_task extends ContextTask {

            final Long existTaskId;
            final Task existTask;
            final Task foundTask;

            public Context_with_matched_task() {
                this.existTask = taskService.createTask(generateNewTask(TASK_TITLE_1));
                this.existTaskId = existTask.getId();

                this.foundTask = taskService.getTask(existTaskId);
            }

            @Test
            @DisplayName("task 내용을 반환한다.")
            void it_returns_single_task() {
                assertThat(this.foundTask).isNotNull();
                assertThat(this.foundTask).isEqualTo(this.existTask);
            }
        }

        @Nested
        @DisplayName("id와 일치하는 값이 없다면")
        class Context_without_matched_task extends ContextTask {

            final Long nonExistPathId;

            public Context_without_matched_task() {
                nonExistPathId = taskService.getLastIdx() + 1;
            }

            @Test
            @DisplayName("Exception 을 반환한다.")
            void it_throws_not_found_exception() {

                assertThatThrownBy(() -> taskService.getTask(nonExistPathId))
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
        class Context_normal_task extends ContextTask {

            final Task notNullOrNotEmptyTitleTask = generateNewTask(TASK_TITLE_1);

            @Test
            @DisplayName("할일 목록에 신규할일을 추가하고, 추가된 할일을 반환한다.")
            void it_adds_new_task_and_returns_added_task() {
                Task created = taskService.createTask(notNullOrNotEmptyTitleTask);

                assertThat(created.getId()).isEqualTo(TASK_ID_1);
                assertThat(created.getTitle()).isEqualTo(TASK_TITLE_1);
            }
        }
    }



    @Nested
    @DisplayName("updateTask() 매소드는")
    class Describe_updateTask {

        @Nested
        @DisplayName("path id 와 일치하는 task 가 존재하고")
        class Context_has_matched_task extends ContextTask {

            final Long pathId;

            public Context_has_matched_task() {
                Task existTask = taskService.createTask(generateNewTask(TASK_TITLE_1));
                pathId = existTask.getId();
            }

            @Nested
            @DisplayName("입력된 task 의 요소가 null이거나 빈값이 아닐때 ")
            class Context_has_valid_task_element {

                final Task inputNotNullOrEmptyTitleTask = generateNewTask(TASK_TITLE_2);

                @Test
                @DisplayName("제목을 수정한 후 수정된 task 를 반환한다.")
                void it_returns_edited_task() {
                    Task updatedTask = taskService.updateTask(pathId, inputNotNullOrEmptyTitleTask);

                    assertThat(updatedTask.getTitle()).isNotEqualTo(TASK_TITLE_1);
                    assertThat(updatedTask.getTitle()).isEqualTo(TASK_TITLE_2);
                }
            }
        }

        @Nested
        @DisplayName("path id 와 일치하는 task 가 존재하지 않으면")
        class Context_has_no_matched_task extends ContextTask {

            final Long nonExistPathId;

            public Context_has_no_matched_task() {
                this.nonExistPathId = taskService.getLastIdx() + 1;
            }

            @Test
            @DisplayName("오류를 반환한다.")
            void it_throws_exception() {
                Task inputTask = generateNewTask(TASK_TITLE_2);

                assertThatThrownBy(() -> taskService.updateTask(nonExistPathId, inputTask))
                        .isInstanceOf(TaskNotFoundException.class)
                        .hasMessageContaining(ERROR_MSG_TASK_NOT_FOUND);
            }
        }
    }


    @Nested
    @DisplayName("deleteTask() 매소드는")
    class Describe_deleteTask {

        @Nested
        @DisplayName("path id 와 일치하는 task를 찾을 수 있을때")
        class Context_has_matched_task extends ContextTask {

            final Task existTask;
            final Long pathId;

            public Context_has_matched_task() {
                this.existTask = taskService.createTask(generateNewTask(TASK_TITLE_1));
                this.pathId = existTask.getId();
            }

            @Test
            @DisplayName("id에 맞는 할일 조회 후 삭제한다.")
            void it_deletes_task() {
                Task deleted = taskService.deleteTask(pathId);

                List<Task> tasks = taskService.getTasks();

                assertThat(tasks).doesNotContain(deleted);
            }
        }

        @Nested
        @DisplayName("path id 와 일치하는 task를 찾을 수 없을때")
        class Context_no_matched_task extends ContextTask {

            final Long nonExistPathId;

            Context_no_matched_task() {
                this.nonExistPathId = taskService.getLastIdx() + 1;
            }

            @Test
            @DisplayName("예외를 던진다.")
            void it_throws_exception() {

                assertThatThrownBy(() -> taskService.deleteTask(nonExistPathId))
                        .isInstanceOf(TaskNotFoundException.class)
                        .hasMessageContaining(ERROR_MSG_TASK_NOT_FOUND);
            }
        }
    }

}
