package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


class TaskServiceTest {

    TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }
    @Test
    @DisplayName("getTasks는 할 일이 비어있으면 빈 리스트를 반환한다.")
    void getFirstTasksIsEmpty() {
        assertThat(taskService.getTasks()).hasSize(0);
    }

    @Test
    @DisplayName("기존에 할일이 2개 있는 경우 2개 조회 테스트")
    void getTasksSuccess() {
        // given
        Task task1 = new Task();
        Task task2 = new Task();

        taskService.createTask(task1);
        taskService.createTask(task2);

        // when & then
        assertThat(taskService.getTasks()).hasSize(2);
    }

    @Test
    @DisplayName("getTasks는 해당하는 할일이 존재하는 경우 상세 할일 내용을 반환한다")
    void getTaskSuccess() {
        // given
        Task task = new Task();
        task.setTitle("task0");
        taskService.createTask(task);

        // when & then
        assertThat(taskService.getTask(1L).getTitle()).isEqualTo("task0");
    }

    @Test
    @DisplayName("getTasks는 할일 상세 조회시 할일이 없는 경우 TaskNotFound 예외를 반환한다")
    void getTaskFail() {
        // when & then
        assertThatThrownBy(() -> taskService.getTask(100L)).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("할일을 생성후 할일이 생성되었는지 확인하는 테스트")
    void createTaskSuccess() {
        // given
        Task task = new Task();
        task.setTitle("task0");

        // when
        Task createdTask = taskService.createTask(task);

        // then
        assertThat(createdTask.getTitle()).isEqualTo("task0");
    }

    @Test
    @DisplayName("할일을 수정후 수정된 할일이 반환되는지 확인하는 테스트")
    void updateTaskSuccess() {
        // given
        Task task = new Task();
        task.setTitle("task0");
        taskService.createTask(task);

        Task updateTask = new Task();
        updateTask.setTitle("task1");

        // when
        Task updatedTask = taskService.updateTask(1L, updateTask);

        // then
        assertThat(updatedTask.getTitle()).isEqualTo("task1");
    }
    @Test
    @DisplayName("존재하지 않는 할일을 업데이트 할 경우 예외를 반환하는 테스트")
    void notExistTaskUpdateFail() {
        // given
        Task task = new Task();
        task.setTitle("task0");
        taskService.createTask(task);

        Task updateTask = new Task();
        updateTask.setTitle("task1");

        // when & then
        assertThatThrownBy(() -> taskService.updateTask(100L, updateTask)).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("할일을 삭제 후 삭제 되었는지 확인하는 테스트")
    void deleteTaskSuccess() {
        // given
        Task task = new Task();
        task.setTitle("task0");
        taskService.createTask(task);

        int oldSize = taskService.getTasks().size();

        // when
        taskService.deleteTask(1L);

        int newSize = taskService.getTasks().size();

        // then
        assertThat(newSize - oldSize).isEqualTo(-1);
    }

    @Test
    @DisplayName("존재하지 않는 할일을 삭제 할 경우 예외를 반환하는 테스트")
    void isNotExistTaskDeleteFail() {
        // given
        Task task = new Task();
        task.setTitle("task0");
        taskService.createTask(task);

        // when & then
        assertThatThrownBy(() -> taskService.deleteTask(100L)).isInstanceOf(TaskNotFoundException.class);
    }

}
