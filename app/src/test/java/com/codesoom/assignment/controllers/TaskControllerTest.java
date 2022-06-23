package com.codesoom.assignment.controllers;

import com.codesoom.assignment.BaseTaskTest;
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

class TaskControllerTest extends BaseTaskTest {
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        TaskService taskService = new TaskService();
        taskController = new TaskController(taskService);
    }

    @Nested
    @DisplayName("list")
    class Describe_list {
        @Nested
        @DisplayName("태스크 리스트에 태스크가 없다면")
        class Context_empty_tasks {
            @Test
            @DisplayName("빈 리스트를 반환한다")
            void it_returns_empty_list() {
                List<Task> tasks = taskController.list();

                assertThat(tasks).hasSize(0);
            }
        }

        @Nested
        @DisplayName("태스크 리스트에 태스크가 있다면")
        class Context_existing_tasks {
            @Test
            @DisplayName("등록된 모든 태스트를 반환한다")
            void it_returns_empty_list() {
                int trial = 10;
                // given
                for (int i = 0; i < trial; i++) {
                    taskController.create(supplyDummyTask(TASK_TITLE_1));
                }
                // when
                List<Task> tasks = taskController.list();
                // then
                assertThat(tasks).hasSize(trial);
            }
        }
    }

    @Nested
    @DisplayName("detail")
    class testDetail {
        @Nested
        @DisplayName("인자로 받은 id 의 태스크가 존재하다면")
        class Context_ExistingID {
            @Test
            @DisplayName("detail() 은 해당 태스크를 리턴한다")
            void it_returns_task() {
                // given
                Task task = taskController.create(supplyDummyTask(TASK_TITLE_1));
                // when
                Task detail = taskController.detail(TASK_ID);
                // then
                assertThat(detail).isNotNull();
                assertThat(detail).isEqualTo(task);

            }
        }

        @Nested
        @DisplayName("인자로 받은 id 의 태스크가 존재하지 않는다면")
        class Context_NonExistingID {
            @Test
            @DisplayName("detail() 은 익셉션을 던진다.")
            void it_returns_task() {
                // given

                assertThatThrownBy(() -> taskController.detail(TASK_ID)) // when
                        .isInstanceOf(TaskNotFoundException.class) //then
                        .hasMessage(supplyErrorMSG(TASK_ID));
            }
        }
    }

    @Nested
    @DisplayName("create")
    class testCreate {
        @Test
        @DisplayName("태스크를 인자로 받으면, 새로 생성된 태스크를 반환한다")
        void it_return_newTask(){
            // given

            // when
            Task task = taskController.create(supplyDummyTask(TASK_TITLE_1));

            // then
            assertThat(task.getId()).isEqualTo(TASK_ID);
            assertThat(task.getTitle()).isEqualTo(TASK_TITLE_1);
        }
    }

    @Nested
    @DisplayName("update")
    class testUpdate {
        @Nested
        @DisplayName("인자로 받은 id와 일치하는 태스크가 등록되어있다면")
        class Context_existingId{
            @Test
            @DisplayName("두번째 인자로 받은 태스크의 타이틀로 id와 일치하는 기존 태스크의 타이틀을 수정하고 수정된 태스크를 반환한다")
            void it_update_exisingTask(){
                // given
                taskController.create(supplyDummyTask(TASK_TITLE_1));

                // when
                Task update = taskController.update(TASK_ID, supplyDummyTask(TASK_TITLE_2));

                // then
                assertThat(update.getTitle()).isEqualTo(TASK_TITLE_2);
                assertThat(update.getId()).isEqualTo(TASK_ID);
            }
        }
        public void update() {

        }
    }

    @Nested
    @DisplayName("patch")
    class testPatch {
        @Test
        public void patch() {

        }
    }

    @Nested
    @DisplayName("delete")
    class testDelete {
        @Test
        public void delete() {

        }
    }
}
