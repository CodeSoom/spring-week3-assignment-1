package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskControllerTest {
    static final String TASK_TITLE = "Test Title";
    static final Long TASK_ID = 1L;
    static final Long INVALID_ID = 12384L;
    static final String UPDATED_TITLE = "Test Title Updated";
    private TaskController controller;
    private TaskService taskService;

    @BeforeEach
    void setup() {
        taskService = new TaskService();
        controller = new TaskController(taskService);
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {
        @Nested
        @DisplayName("저장되어 있는 task가 없다면")
        class No_task {
            @Test
            @DisplayName("빈 리스트를 리턴한다.")
            void it_return_empty_List() {
                assertThat(controller.list()).isEqualTo(new ArrayList());
            }

        }

        @Nested
        @DisplayName("만약 task가 3개 있으면")
        class Has_task {
            final static int TASK_SIZE = 3;

            @BeforeEach
            void setup() {
                Task task = new Task();
                for (int i = 0; i < TASK_SIZE; i++) {
                    task.setTitle("test title-" + i);
                    controller.create(task);
                }
            }

            @Test
            @DisplayName("크기 3의 리스트를 리턴한다.")
            void it_retruns_tasks() {
                assertThat(controller.list()).hasSize(TASK_SIZE);
            }
        }
    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detail {
        @Nested
        @DisplayName("만약 저장되어 있지 않는 task의 ID가 주어지면")
        class No_task {

            @Test
            @DisplayName("TasksNotFoundException을 던진다.")
            void it_throws_TaskNotFoundException() {
                assertThatThrownBy(() -> controller.detail(INVALID_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있는 task의 ID가 주어지면")
        class Has_task {
            @BeforeEach
            void setup() {
                Task task = new Task();
                task.setTitle(TASK_TITLE);
                controller.create(task);
            }

            @Test
            @DisplayName("찾은 task를 리턴한다.")
            void return_found_task() {
                assertThat(controller.detail(TASK_ID).getId()).isEqualTo(TASK_ID);
                assertThat(controller.detail(TASK_ID).getTitle()).isEqualTo(TASK_TITLE);
            }
        }
    }
    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {
        @Nested
        @DisplayName("만약 task가 주어지면")
        class Create_Task {
            Task task;

            @BeforeEach
            void setup() {
                task = new Task();
                task.setTitle(TASK_TITLE);
                controller = new TaskController(new TaskService());
            }

            @Test
            @DisplayName("주어진 task를 등록하고 등록한 task를 리턴한다.")
            void it_return_created_task() {
                assertThatThrownBy(() -> controller.detail(TASK_ID))
                        .isInstanceOf(TaskNotFoundException.class);
                controller.create(task);
                assertThat(controller.detail(TASK_ID).getTitle()).isEqualTo(TASK_TITLE);
            }

        }
    }
    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {
        Task source;
        @BeforeEach
        void setup() {
            Task savedtask = new Task();
            savedtask.setTitle(TASK_TITLE);
            controller.create(savedtask);
            source = new Task();
            source.setTitle(UPDATED_TITLE);
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않는 task의 ID가 주어지면")
        class No_task {

            @Test
            @DisplayName("TasksNotFoundException을 던진다.")
            void it_throws_TaskNotFoundException() {
                assertThatThrownBy(() -> controller.update(INVALID_ID,source))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있는 task의 ID가 주어지면")
        class Has_task {
            @Test
            @DisplayName("task를 업데이트 하고 업데이트된 task를 리턴한다.")
            void update_task() {
                assertThat(controller.update(TASK_ID,source).getId()).isEqualTo(TASK_ID);
                assertThat(controller.update(TASK_ID,source).getTitle()).isEqualTo(UPDATED_TITLE);
            }
        }

    }
    @Nested
    @DisplayName("patch 메소드는")
    class Describe_patch {
        Task source;
        @BeforeEach
        void setup() {
            Task savedtask = new Task();
            savedtask.setTitle(TASK_TITLE);
            controller.create(savedtask);
            source = new Task();
            source.setTitle(UPDATED_TITLE);
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않는 task의 ID가 주어지면")
        class No_task {

            @Test
            @DisplayName("TasksNotFoundException을 던진다.")
            void it_throws_TaskNotFoundException() {
                assertThatThrownBy(() -> controller.patch(INVALID_ID,source))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있는 task의 ID가 주어지면")
        class Has_task {
            @Test
            @DisplayName("task를 업데이트 하고 업데이트된 task를 리턴한다.")
            void update_task() {
                assertThat(controller.patch(TASK_ID,source).getId()).isEqualTo(TASK_ID);
                assertThat(controller.patch(TASK_ID,source).getTitle()).isEqualTo(UPDATED_TITLE);
            }
        }

    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {
        @BeforeEach
        void setup() {
            Task savedtask = new Task();
            savedtask.setTitle(TASK_TITLE);
            controller.create(savedtask);
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않는 task의 ID가 주어지면")
        class No_task {

            @Test
            @DisplayName("TasksNotFoundException을 던진다.")
            void it_throws_TaskNotFoundException() {
                assertThatThrownBy(() -> controller.delete(INVALID_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있는 task의 ID가 주어지면")
        class has_task {
            @Test
            @DisplayName("task를 삭제한다.")
            void delete_task() {
                assertThat(controller.detail(TASK_ID).getId()).isEqualTo(TASK_ID);
                controller.delete(TASK_ID);
                assertThatThrownBy(() -> controller.detail(TASK_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}