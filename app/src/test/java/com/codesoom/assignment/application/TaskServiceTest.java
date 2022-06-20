package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TaskService 클래스")
class TaskServiceTest {
    static final String TASK_TITLE = "Test Title";
    static final Long TASK_ID = 1L;
    static final Long INVALID_ID = 12384L;
    static final String UPDATED_TITLE= "Test Title Updated";
    private TaskService service;

    @BeforeEach
    void setUp() {
        service = new TaskService();

    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {
        @Nested
        @DisplayName("만약 task가 없다면")
        class No_task {
            @Test
            @DisplayName("빈 리스트를 리턴한다.")
            void it_return_empty_List() {
                assertThat(service.getTasks()).isEqualTo(new ArrayList());
            }

        }

        @Nested
        @DisplayName("만약 task가 3개 있으면")
        class Have_task {
            final static int TASK_SIZE = 3;

            @BeforeEach
            void setup() {
                Task task = new Task();
                for (int i = 0; i < TASK_SIZE; i++) {
                    task.setTitle("test title-" + i);
                    service.createTask(task);
                }
            }

            @Test
            @DisplayName("크기 3의 리스트를 반환한다.")
            void it_retruns_tasks() {
                assertThat(service.getTasks()).hasSize(TASK_SIZE);
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {
        @Nested
        @DisplayName("만약 요청한 Id에 해당하는 task를 찾지 못하면")
        class No_task {

            @Test
            @DisplayName("TasksNotFoundException을 발생시킨다.")
            void it_throws_TaskNotFoundException() {
                assertThatThrownBy(() -> service.getTask(INVALID_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("만약 요청한 Id에 해당하는 task를 찾으면")
        class has_task {
            @BeforeEach
            void setup() {
                Task task = new Task();
                task.setTitle(TASK_TITLE);
                service.createTask(task);
            }

            @Test
            @DisplayName("찾은 task를 반환한다.")
            void return_found_task() {
                assertThat(service.getTask(TASK_ID).getId()).isEqualTo(TASK_ID);
                assertThat(service.getTask(TASK_ID).getTitle()).isEqualTo(TASK_TITLE);
            }
        }

    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_createTask {
        @Nested
        @DisplayName("만약 task가 등록되었을 때")
        class create_Task {
            Task task;

            @BeforeEach
            void setup() {
                task = new Task();
                task.setTitle(TASK_TITLE);
            }

            @Test
            @DisplayName("등록한 Task를 리턴한다.")
            void it_return_created_task() {
                assertThat(service.createTask(task).getTitle()).isEqualTo(TASK_TITLE);
            }

            @Test
            @DisplayName("getTask를 통해 찾을 수 있다. ")
            void it_can_be_founded_by_getTask() {
                assertThatThrownBy(() -> service.getTask(TASK_ID))
                        .isInstanceOf(TaskNotFoundException.class);
                service.createTask(task);
                assertThat(service.getTask(TASK_ID).getTitle()).isEqualTo(TASK_TITLE);
            }
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {
        Task source;
        @BeforeEach
        void setup() {
            Task savedtask = new Task();
            savedtask.setTitle(TASK_TITLE);
            savedtask.setId(1L);
            service.createTask(savedtask);
            source = new Task();
            source.setTitle(UPDATED_TITLE);
        }

        @Nested
        @DisplayName("만약 요청한 Id에 해당하는 task를 찾지 못하면")
        class No_task {

            @Test
            @DisplayName("TasksNotFoundException을 발생시킨다.")
            void it_throws_TaskNotFoundException() {
                assertThatThrownBy(() -> service.updateTask(INVALID_ID,source))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("만약 요청한 Id에 해당하는 task를 찾으면")
        class has_task {
            @Test
            @DisplayName("task를 업데이트 한다.")
            void update_task() {
                assertThat(service.updateTask(TASK_ID,source).getId()).isEqualTo(TASK_ID);
                assertThat(service.updateTask(TASK_ID,source).getTitle()).isEqualTo(UPDATED_TITLE);
            }
        }

    }

    @Test
    void deleteTask() {
    }
}