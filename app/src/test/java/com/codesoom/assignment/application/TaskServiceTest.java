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
import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    private static final String TASK_TITLE = "test";
    private static final String UPDATE_POSTFIX = "!!!";

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        // subject
        taskService = new TaskService();

        //fixtures
        Task task = new Task();
        task.setTitle(TASK_TITLE);

        taskService.createTask(task);
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks{
        @Nested
        @DisplayName("Task가 등록되어 있다면")
        class Context_have_task{
            @Test
            @DisplayName("등록된 모든 Task를 리턴한다")
            void It_return_tasks(){
                List<Task> tasks = taskService.getTasks();

                assertThat(tasks).hasSize(1);

                Task task = tasks.get(0);
                assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
            }
        }
        @Nested
        @DisplayName("Task가 등록되어 있지 않다면")
        class Context_have_not_task{
            @Test
            @DisplayName("비어 있는 리스트를 리턴한다")
            void it_return_tasks(){
                List<Task> tasks = taskService.getTasks();

                assertThat(tasks).hasSize(1);

                Task task = tasks.get(0);
                assertThat(task.getTitle()).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask{
        @Nested
        @DisplayName("등록된 Task의 id가 주어지면")
        class Context_withValid_id{
            @Test
            @DisplayName("해당 Task를 리턴한다")
            void It_return_task(){
                Task found = taskService.getTask(1L);
                assertThat(found.getTitle()).isEqualTo(TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("Task를 찾을 수 없는 id가 주어지면")
        class Context_withInvalid_id{
            @Test
            @DisplayName("테스크를 찾을 수 없다는 내용의 예외를 던진다.")
            void It_return_error(){
                assertThatThrownBy(()-> taskService.getTask(100L)).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_createTask{
        @Nested
        @DisplayName("등록할 Task가 주어진다면")
        class Context_with_task{
            @Test
            @DisplayName("Task를 생성하고 리턴한다.")
            void It_return_task(){
                int oldSize = taskService.getTasks().size();
                Task task = new Task();
                task.setTitle(TASK_TITLE);

                taskService.createTask(task);

                int newSize = taskService.getTasks().size();

                assertThat(newSize - oldSize).isEqualTo(1);
            }
        }
        @Nested
        @DisplayName("등록할 Task가 주어지지 않는다면")
        class Context_without_task{
            @Test
            @DisplayName("내용이 없다는 예외를 던진다.")
            void it_return_error(){
                Task task = null;

                assertThatThrownBy(()-> taskService.createTask(task)).isInstanceOf(NullPointerException.class);
            }
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask{
        @Nested
        @DisplayName("등록된 태스크의 id가 주어진다면")
        class Context_withValid_id{
            @Test
            @DisplayName("등록되어있는 Task를 바꾸고 리턴한다.")
            void it_return_task(){
                Task source = new Task();
                source.setTitle(TASK_TITLE + UPDATE_POSTFIX);
                taskService.updateTask(1L, source);

                Task task = taskService.getTask(1L);
                assertThat(task.getTitle()).isEqualTo(TASK_TITLE + UPDATE_POSTFIX);
            }
        }
        @Nested
        @DisplayName("등록되지 않은 Task의 id가 주어진다면")
        class Context_withInvalid_id{
            @Test
            @DisplayName("Task를 찾을수 없다는 예외를 던진다.")
            void It_return_error(){
                Task source = new Task();
                source.setTitle(TASK_TITLE + UPDATE_POSTFIX);
                assertThatThrownBy(()-> taskService.updateTask(100L, source)).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

//    @Nested
//    @DisplayName("deleteTask 메서드는")
//    class Describe_deleteTask{
//        @Nested
//        @DisplayName("id값이 올바르면")
//        class Context_withValid_id{
//            @Test
//            @DisplayName("")
//        }
//    }
}
