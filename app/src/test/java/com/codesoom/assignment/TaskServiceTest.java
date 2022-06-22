package com.codesoom.assignment;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService 클래스")
public class TaskServiceTest extends TestHelper {
    private TaskService service;

    private final Task dummyTask1 = dummyTask(1L, "1");
    private final Task dummyTask2 = dummyTask(2L, "2");
    private final Task dummyTask3 = dummyTask(3L, "3");

    private Task subject1() {
        return service.createTask(dummyTask1);
    }

    private Task subject2() {
        return service.createTask(dummyTask2);
    }

    private Task subject3() {
        return service.createTask(dummyTask3);
    }

    @BeforeEach
    private void setUp() {
        this.service = new TaskService();
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_createTasks {

        @Nested
        @DisplayName("아무런 task가 등록되지 않았다면")
        class Context_no_tasks {
            @Test
            @DisplayName("빈 리스트를 리턴한다")
            void it_returns_empty_list() {
                List<Task> tasks = service.getTasks();

                assertThat(tasks).hasSize(0);
            }
        }

        @Nested
        @DisplayName("1개의 task만 등록되어 있다면")
        class Context_only_one_task {
            @BeforeEach
            void setup() {
                subject1();
            }

            @Test
            @DisplayName("등록된 1개의 task만이 들어있는 리스트를 리턴한다")
            void it_returns_empty_list() {
                List<Task> tasks = service.getTasks();

                assertThat(tasks).hasSize(1);
                assertThat(tasks.get(0).getTitle()).isEqualTo(dummyTask1.getTitle());
            }
        }

        @Nested
        @DisplayName("3개의 task만 등록되어 있다면")
        class Context_multiple_tasks {
            @BeforeEach
            void setup() {
                subject1();
                subject2();
                subject3();
            }

            @Test
            @DisplayName("등록된 3개의 task만이 들어있는 리스트를 리턴한다")
            void it_returns_empty_list() {
                List<Task> tasks = service.getTasks();

                assertThat(tasks).hasSize(3);
                assertThat(tasks.get(0).getTitle()).isEqualTo(dummyTask1.getTitle());
                assertThat(tasks.get(1).getTitle()).isEqualTo(dummyTask2.getTitle());
                assertThat(tasks.get(2).getTitle()).isEqualTo(dummyTask3.getTitle());
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {
        @Nested
        @DisplayName("찾는 ID가 없다면")
        class Context_not_found_with_id {
            @Test
            @DisplayName("TaskNotFoundException을 던진다")
            void it_throws_TaskNotFoundException() {
                final Long UNREGISTERED_ID = Long.MAX_VALUE;

                assertThatThrownBy(() -> service.getTask(UNREGISTERED_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
        
        @Nested
        @DisplayName("찾는 ID가 있다면")
        class Context_found_with_id {
            private Task task;

            @BeforeEach
            void setUp() {
                task = subject1();
            }

            @Test
            @DisplayName("해당 ID를 가진 Task를 리턴한다")
            void it_returns_the_task() {
                assertThat(service.getTask(task.getId())).isEqualTo(task);
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_createTask {
        @Nested
        @DisplayName("Task 타입의 object를 인자로 받아")
        class Context_normal {

            @Test
            @DisplayName("Task를 추가하고, 추가된 Task를 반환한다")
            void it_returns_added_task() {
                Task task = subject1();

                assertThat(task.getId()).isInstanceOf(Long.class);
                assertThat(task.getTitle()).isEqualTo(dummyTask1.getTitle());
            }
        }

        @Nested
        @DisplayName("여러 task를 추가해도")
        class Context_register_many_tasks {
            private Task task1;
            private Task task2;
            private Task task3;

            @BeforeEach
            void setUp() {
                task1 = subject1();
                task2 = subject2();
                task3 = subject3();
            }

            @Test
            @DisplayName("모두 다른 ID를 부여한다")
            void it_gives_tasks_all_different_IDs() {
                assertThat(task1.getId()).isNotEqualTo(task2.getId());
                assertThat(task2.getId()).isNotEqualTo(task3.getId());
                assertThat(task3.getId()).isNotEqualTo(task1.getId());
            }
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {
        @Nested
        @DisplayName("찾는 ID가 없다면")
        class Context_not_found_with_id {
            @Test
            @DisplayName("TaskNotFoundException을 던진다")
            void it_throws_TaskNotFoundException() {
                final Long UNREGISTERED_ID = Long.MAX_VALUE;

                assertThatThrownBy(() -> service.updateTask(UNREGISTERED_ID, dummyTask1))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("찾는 ID가 있다면")
        class Context_found_with_id {
            private Task task;

            @BeforeEach
            void setUp() {
                task = subject1();
            }

            @Test
            @DisplayName("해당 ID를 가진 Task의 title을 새로운 title로 수정한다")
            void it_updates_found_task() {
                Task updatedTask = service.updateTask(task.getId(), dummyTask2);

                assertThat(updatedTask.getId()).isEqualTo(task.getId());
                assertThat(updatedTask.getTitle()).isEqualTo(dummyTask2.getTitle());
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_deleteTask {
        @Nested
        @DisplayName("찾는 ID가 없다면")
        class Context_not_found_with_id {
            @Test
            @DisplayName("TaskNotFoundException을 던진다")
            void it_throws_TaskNotFoundException() {
                final Long UNREGISTERED_ID = Long.MAX_VALUE;

                assertThatThrownBy(() -> service.deleteTask(UNREGISTERED_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("찾는 ID가 있다면")
        class Context_found_with_id {
            private Task task;

            @BeforeEach
            void setUp() {
                task = subject1();
            }

            @Test
            @DisplayName("해당 ID를 가진 task를 삭제한다")
            void it_delete_found_task() {
                Task deletedTask = service.deleteTask(task.getId());

                assertThat(deletedTask.getId()).isEqualTo(task.getId());
                assertThatThrownBy(() -> service.getTask(deletedTask.getId()))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }


}
