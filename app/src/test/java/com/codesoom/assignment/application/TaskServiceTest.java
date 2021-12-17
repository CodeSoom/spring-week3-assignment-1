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
        @DisplayName("모든")
        class Context_everything{
            @Test
            @DisplayName("Task를 가져온다")
            void It_return_tasks(){
                List<Task> tasks = taskService.getTasks();

                assertThat(tasks).hasSize(1);

                Task task = tasks.get(0);
                assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask{
        @Nested
        @DisplayName("Id가 있으면")
        class Context_withValid_id{
            @Test
            @DisplayName("해당 정보를 가져온다")
            void It_return_task(){
                Task found = taskService.getTask(1L);
                assertThat(found.getTitle()).isEqualTo(TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("Id가 없으면")
        class Context_withInvalid_id{
            @Test
            @DisplayName("에러가 발상핸다")
            void It_return_error(){
                assertThatThrownBy(()-> taskService.getTask(100L)).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("createTask는")
    class Describe_createTask{
        @Nested
        @DisplayName("새로운 Task가 있다면")
        class Context_with_task{
            @Test
            @DisplayName("Task를 생성한다")
            void It_return_task(){
                int oldSize = taskService.getTasks().size();
                Task task = new Task();
                task.setTitle(TASK_TITLE);

                taskService.createTask(task);

                int newSize = taskService.getTasks().size();

                assertThat(newSize - oldSize).isEqualTo(1);
            }
        }
        @Test
        @DisplayName("Task가 없다면")
        void it_return_error(){
            Task task = null;

            assertThatThrownBy(()-> taskService.createTask(task)).isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask{
        @Nested
        @DisplayName("올바른 id가 주어진다면")
        class Context_withValid_id{
            @Test
            @DisplayName("바뀐 값이 리턴한다")
            void it_return_task(){
                Task source = new Task();
                source.setTitle(TASK_TITLE + UPDATE_POSTFIX);
                taskService.updateTask(1L, source);

                Task task = taskService.getTask(1L);
                assertThat(task.getTitle()).isEqualTo(TASK_TITLE + UPDATE_POSTFIX);
            }
        }
        @Nested
        @DisplayName("id가 올바르지 않다면")
        class Context_withInvalid_id{
            @Test
            @DisplayName("에러가 발상핸다")
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
