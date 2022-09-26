package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DisplayName("TaskService 클래스")
class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {

        @Nested
        @DisplayName("DB에 데이터가 없는데 조회를 한다면")
        class Context_with_empty_db {
            @Test
            @DisplayName("빈 ArrayList를 리턴한다")
            void it_returns_empty_arraylist() {
                List<Task> findTasks = taskService.getTasks();

                assertThat(findTasks).hasSize(0);
                assertThat(findTasks).isEqualTo(new ArrayList<>());
            }
        }

        @Nested
        @DisplayName("DB에 데이터가 있는데 조회를 한다면")
        class Context_with_non_empty_db {
            @Test
            @DisplayName("등록된 모든 할 일을 리턴한다")
            void it_returns_all_tasks() {
                // given
                Task task1 = new Task();
                task1.setTitle("할 일 추가1");

                Task task2 = new Task();
                task2.setTitle("할 일 추가2");


                taskService.createTask(task1);
                taskService.createTask(task2);

                // when
                List<Task> findTasks = taskService.getTasks();

                final int expectSize = 2;
                final int actualSize = findTasks.size();

                // then
                assertThat(actualSize).isEqualTo(expectSize);
                assertThat(findTasks.get(0).getTitle()).isEqualTo(task1.getTitle());
                assertThat(findTasks.get(1).getTitle()).isEqualTo(task2.getTitle());
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_createTask {
        @Nested
        @DisplayName("새로운 할 일을 추가한다면")
        class Context_with_new_task {
            @Test
            @DisplayName("DB에 등록 후 등록된 할 일을 리턴한다")
            void it_returns_registered_task() {
                // given
                Task task = new Task();
                task.setId(1L);
                task.setTitle("할 일 추가1");

                // when
                Task actualTask = taskService.createTask(task);

                // then
                assertThat(actualTask.equals(task)).isEqualTo(true);
                assertThat(taskService.getTasks()).hasSize(1);
                assertThat(taskService.getTask(1L).getTitle()).isEqualTo("할 일 추가1");
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {
        @Nested
        @DisplayName("존재하는 할 일 ID로 검색한다면")
        class Context_with_existing_taskid {
            final Task task = new Task();

            @BeforeEach
            void insertTask() {
                task.setId(1L);
                task.setTitle("할 일 추가1");

                taskService.createTask(task);
            }

            @Test
            @DisplayName("검색된 할 일을 리턴한다")
            void it_returns_found_task() {
                // given
                // when
                Task findTask = taskService.getTask(1L);

                // then
                assertThat(findTask.equals(task)).isEqualTo(true);
                assertThat(findTask.getTitle()).isEqualTo("할 일 추가1");
            }

        }

        @Nested
        @DisplayName("존재하지않는 할 일 ID로 검색한다면")
        class Context_with_non_existing_taskid {
            @Test
            @DisplayName("TaskNotFoundException 예외가 발생한다")
            void it_throws_tasknotfoundexception() {
                assertThatThrownBy(() -> taskService.getTask(100L)).isInstanceOf(TaskNotFoundException.class);
            }
        }

    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {
        @Nested
        @DisplayName("존재하는 할 일 ID로 수정요청한다면")
        class Describe_with_existing_taskid {
            final Task task = new Task();

            @BeforeEach
            void insertTask() {
                task.setId(1L);
                task.setTitle("할 일 추가1");

                taskService.createTask(task);
            }

            @Test
            @DisplayName("DB를 수정 후 수정된 할 일을 리턴한다")
            void it_returns_modified_task() {
                // given
                Task newTask = new Task();
                newTask.setId(1L);
                newTask.setTitle("할 일 수정1");

                // when
                Task updatedTask = taskService.updateTask(1L, newTask);

                // then
                assertThat(updatedTask.equals(newTask)).isEqualTo(true);
                assertThat(updatedTask.getTitle()).isEqualTo(newTask.getTitle());
            }
        }

        @Nested
        @DisplayName("존재하지않는 할 일 ID로 수정요청한다면")
        class Describe_with_non_existing_taskid {
            @Test
            @DisplayName("TaskNotFoundException 예외가 발생한다")
            void it_throws_tasknotfoundexception() {
                Task newTask = new Task();
                newTask.setId(100L);
                newTask.setTitle("할 일 수정100");

                assertThatThrownBy(() -> taskService.updateTask(100L, newTask)).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_deleteTask {
        @Nested
        @DisplayName("존재하는 할 일 ID로 삭제요청한다면")
        class Describe_with_existing_taskid {
            final Task task = new Task();

            @BeforeEach
            void insertTask() {
                task.setId(1L);
                task.setTitle("할 일 추가1");

                taskService.createTask(task);
            }

            @Test
            @DisplayName("DB를 삭제 후 삭제된 할 일을 리턴한다")
            void it_returns_modified_task() {
                final int beforeTotalCount = taskService.getTasks().size();
                final Task expectedTask = taskService.getTask(1L);

                // when
                Task deletedTask = taskService.deleteTask(1L);
                final int afterTotalCount = taskService.getTasks().size();

                // then
                assertThat(deletedTask.equals(task)).isEqualTo(true);
                assertThat(afterTotalCount).isEqualTo(beforeTotalCount - 1);
                assertThat(deletedTask).isEqualTo(expectedTask);
            }
        }

        @Nested
        @DisplayName("존재하지않는 할 일 ID로 삭제요청한다면")
        class Describe_with_non_existing_taskid {
            @Test
            @DisplayName("TaskNotFoundException 예외가 발생한다")
            void it_throws_tasknotfoundexception() {
                assertThatThrownBy(() -> taskService.deleteTask(100L)).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}