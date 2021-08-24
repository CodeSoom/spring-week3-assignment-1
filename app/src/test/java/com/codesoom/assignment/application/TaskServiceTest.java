package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService 클래스")
class TaskServiceTest {
    private TaskService taskService;
    private static final String TASK_TITLE = "test";
    private static final Long NOT_EXIST_ID = 999999999L;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {
        @Nested
        @DisplayName("등록된 할 일이 없다면")
        class Context_no_have_tasks {
            @Test
            @DisplayName("비어있는 리스트를 리턴한다")
            void it_returns_emptyTask() {
                List<Task> tasks = taskService.getTasks();
                assertThat(tasks).isEmpty();
            }
        }

        @Nested
        @DisplayName("등록된 할 일이 있다면")
        class Context_have_tasks {
            @Test
            @DisplayName("할 일들을 리턴한다")
            void it_returns_tasks() {
                Task task1 = new Task();
                Task task2 = new Task();

                taskService.createTask(task1);
                taskService.createTask(task2);

                // when
                List<Task> tasks = taskService.getTasks();

                //then
                assertThat(tasks.size()).isEqualTo(2);
                assertThat(tasks).contains(task1, task2);
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {
        @Nested
        @DisplayName("입력받은 id와 일치하는 등록된 할 일이 없다면")
        class Context_matchId_NotExist {
            @Test
            @DisplayName("할 일을 찾을 수 없다는 에러를 발생시킨다.")
            void it_returns_TaskNotFoundException() {
                assertThatThrownBy(() -> taskService.getTask(NOT_EXIST_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("입력받은 id와 일치하는 등록된 할 일이 있다면")
        class Context_matchId_exist {
            @Test
            @DisplayName("할 일을 리턴한다")
            void it_returns_task() {
                //given
                Task task = new Task();
                taskService.createTask(task);

                //when
                Task foundTask = taskService.getTask(1L);

                //then
                assertThat(foundTask).isEqualTo(task);
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_createTask {
        @Nested
        @DisplayName("할 일을 생성했다면")
        class Context_create_a_task {
            @Test
            @DisplayName("생성한 할 일을 반환합니다.")
            void createTask() {
                //given
                Task task = new Task();
                task.setTitle(TASK_TITLE);

                //when
                Task createdTask = taskService.createTask(task);

                //then
                Task foundItem = taskService.getTask(createdTask.getId());
                assertThat(foundItem).isEqualTo(createdTask);
            }
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {
        @Nested
        @DisplayName("입력받은 id와 일치하는 등록된 할 일이 있다면")
        class Context_matchId_exist {
            @Test
            @DisplayName("입력받은 제목으로 수정된 할 일을 리턴 합니다")
            void updateTask() {
                //given
                Task task = new Task();
                task.setTitle(TASK_TITLE);

                Task createdTask = taskService.createTask(task);
                Long itemId = createdTask.getId();

                //when
                Task updateTask = new Task();
                updateTask.setTitle("update title");
                taskService.updateTask(itemId, updateTask);

                //then
                Task foundItem = taskService.getTask(itemId);
                assertThat(foundItem.getTitle()).isEqualTo(updateTask.getTitle());
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_deleteTask {
        @Nested
        @DisplayName("입력받은 id와 일치하는 등록된 할 일이 있다면")
        class Context_matchId_exist {
            @Test
            @DisplayName("할 일을 삭제합니다.")
            void deleteTask() {
                //given
                Task task = new Task();
                Task createdTask = taskService.createTask(task);

                //when
                taskService.deleteTask(createdTask.getId());

                //then
                List<Task> tasks = taskService.getTasks();
                assertThat(tasks).doesNotContain(task);
            }
        }

    }

}
