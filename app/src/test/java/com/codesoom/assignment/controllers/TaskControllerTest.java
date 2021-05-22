package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@DisplayName("TaskController 클래스")
class TaskControllerTest {

    private TaskController taskController;

    /**
     * mock object : 타입이 필요
     * spy -> Proxy : 진짜 오브젝트가 필요함
     */
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = mock(TaskService.class);
        List<Task> tasks = new ArrayList<>();

        Task task = new Task();
        task.setTitle("hello");
        tasks.add(task);

        given(taskService.getTasks()).willReturn(tasks);
        given(taskService.getTask(100L)).willThrow(new TaskNotFoundException(100L));
        given(taskService.updateTask(eq(100L),any(Task.class))).willThrow(new TaskNotFoundException(100L));
    }

    @Test
    @DisplayName("목록은 처음에는 빈 목록이어야한다.")
    void listWithoutTasks(){

        given(taskService.getTasks()).willReturn(new ArrayList<>());
        TaskController controller = new TaskController();
        assertThat(controller.list()).isEmpty();
    }

    @Test
    @DisplayName("task 가 추가되었다면 리스트가 채워지게 된다.")
    void listWithSomeTasks(){
        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setId(1L);
        task.setTitle("hello");
        tasks.add(task);

        given(taskService.getTasks()).willReturn(tasks);

        assertThat(taskController.list()).isNotEmpty();
    }

    @Test
    @DisplayName("detail과 getTask()는 역할이 동일하다.")
    public void details(){
        Task task = new Task();
        task.setId(1L);
        task.setTitle("hello");
        taskController.create(task);

        given(taskService.getTask(1L)).willReturn(task);

        Task findTask = taskController.detail(1L);

        assertThat(findTask).isEqualTo(task);
    }
    @Test
    @DisplayName("찾으려는 아이디 값이 없다면, 에러를 리턴한다.")
    public void detailsIsNull(){
        given(taskService.getTask(1L)).willReturn(null);
        assertThat(taskController.detail(1L)).isNull();

        assertThatThrownBy(() -> taskController.detail(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("task를 만들었다면, 더이상 빈 목록은 아니게 된다.")
    void createNewTest(){
        TaskController controller = new TaskController();
        Task task = new Task();

        task.setTitle("hello");
        controller.create(task);
        assertThat(controller.list()).isNotEmpty();
        assertThat(controller.list()).hasSize(1);

        task.setTitle("hello2");
        controller.create(task);
        assertThat(controller.list()).hasSize(2);
    }

    @Test
    public void updateTask(){
        Task task = new Task();
        task.setTitle("hello");

        assertThatThrownBy(() -> taskController.update(100L,task))
                .isInstanceOf(TaskNotFoundException.class);
    }
}
