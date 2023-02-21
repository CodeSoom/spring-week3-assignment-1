package com.codesoom.assignment.controllers;

import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
class TaskControllerTest {
    private TaskController taskController;

    private TaskService taskService = new TaskService();
    private static final String DUMMY_TITLE = "dummy_1";

    @BeforeEach
    public void init(){
        taskController = new TaskController(taskService);
        Task task1 = new Task();
        task1.setTitle(DUMMY_TITLE);

        taskController.create(task1);
    }

    @DisplayName("/tasks Process")
    @Nested
    class ListTaskController{

        @DisplayName("/tasks 테스트")
        @Test
        public void list() {
            TaskService service = new TaskService();
            TaskController controller = new TaskController(taskService);
            assertThat(controller.list()).isNotEmpty();
        }

        @DisplayName("detail 검색")
        @Test
        public void PathListValid(){
            //when
            assertThat(taskController.detail(1L).getTitle()).isEqualTo(DUMMY_TITLE);
        }

        @DisplayName("detailInValidId 오류 확인")
        @Test
        public void PathListInvalid(){
            assertThatExceptionOfType(TaskNotFoundException.class)
                    .isThrownBy(()->{
                        taskController.detail(100L);
                    }).withMessage("Task not found: 100");
        }
    }


    @DisplayName("Create Task Process")
    @Nested
    class CreateProcess{

        @DisplayName("valid Create Task")
        @Test
        public void createNewTask() {
            assertThat(taskController.list()).isNotEmpty();
        }

        @DisplayName("Invalid Create Task")
        @Test
        public void InvalidCreateTask() {
            assertThat(taskController.create(new Task()).getTitle()).isNull();
        }


    }

    @DisplayName("update Process")
    @Nested
    class UpdateProcess{

        @DisplayName("update getId")
        @Test
        public void update() {
            //given
            Task task1 = new Task();
            task1.setTitle("new Task");
            taskController.update(1L , task1);
            //when
            assertThat(taskController.list().get(0).getTitle()).isEqualTo("new Task");
            //Then
        }
    }

    @DisplayName("Delete Task Process")
    @Nested
    class DeleteTaskController{

        @DisplayName("Delete Task 확인")
        @Test
        public void deleteController(){
            //when
            taskController.delete(1L);
            //Then
            assertThat(taskController.list()).isEmpty();
        }
    }
    @DisplayName("InvalidId Delete Task 예외 확인")
    @Test
    public void deleteInvalidId(){
         assertThatExceptionOfType(TaskNotFoundException.class)
                 .isThrownBy(()->{
                     taskController.delete(100L);
                 }).withMessageNotContainingAny("Task not found 100");
    }

    @Test
    public void allProcess() throws Exception{
        assertThat(taskController.list()).isNotEmpty();
        assertThat(taskController.detail(1L).getTitle()).isEqualTo(DUMMY_TITLE);

        Task newTask = new Task();
        newTask.setTitle("Dummy2");

        taskController.create(newTask);
        assertThat(taskController.list().size()).isEqualTo(2);

        newTask.setTitle("Dummy3");
        taskController.update(1L,newTask );

        assertThat(taskController.list().get(0).getTitle()).isEqualTo("Dummy3");

        taskController.delete(1L);
        assertThat(taskController.list().size()).isEqualTo(1);
    }
}
