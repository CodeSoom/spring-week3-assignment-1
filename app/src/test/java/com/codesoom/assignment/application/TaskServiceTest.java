package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService 클래스")
class TaskServiceTest {

    private TaskService taskService;
    private static final String TASK_TITLE = "test";

    @BeforeEach
    public void setUp(){
        taskService = new TaskService();
    }

    @Nested
    @DisplayName("GET")
    class getMethod {
        @Nested
        @DisplayName("tasks")
        class taskService_list{
            @Test
            @DisplayName("목록은 처음에는 빈 목록이어야한다.")
            public void getTasks(){
                assertThat(taskService.getTasks()).isEmpty();
            }

            @Test
            @DisplayName("Task가 추가되었다면, 리스트는 빈 배열이 아니다.")
            public void getTaskWithValidId(){
                Task generatedTask = generatedTask();

                String foundTitle = taskService.getTask(1L).getTitle();
                assertThat(foundTitle).isEqualTo(generatedTask.getTitle());

            }


        }

        @Nested
        @DisplayName("tasks/{id}")
        class taskService_details{
            @Test
            @DisplayName("아이디를 찾을 때 해당 값이 없다면 에러를 발생시킨다.")
            public void getTaskWithInvalidId(){
                assertThatThrownBy(() -> taskService.getTask(100L)).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("POST")
    class post_method{
        @Test
        public void postTask(){
            generatedTask();
            assertThat(taskService.getTasks()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("DELETE")
    class delete_method{

        @Test
        public void deleteTask(){
            int oldSize = taskService.getTasks().size();
            taskService.createTask(generatedTask());

            taskService.deleteTask(1L);
            int newSize = taskService.getTasks().size();
            assertThat(newSize-oldSize).isEqualTo(1);
        }
    }
    @Nested
    @DisplayName("PUT")
    class update_method{

        @Test
        public void updateTask(){
            taskService.createTask(generatedTask());
            Task putTask = generatedTask();
            putTask.setTitle("put test");
            taskService.updateTask(1L,putTask);
            assertThat(taskService.getTasks().get(0).getTitle()).isEqualTo("put test");
        }
    }

    public Task generatedTask() {
        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);
        return task;
    }

    @Test
    public void deleteTaskWithNotExistedId(){
        assertThatThrownBy(() -> taskService.deleteTask(1L)).isInstanceOf(TaskNotFoundException.class);
    }
}
