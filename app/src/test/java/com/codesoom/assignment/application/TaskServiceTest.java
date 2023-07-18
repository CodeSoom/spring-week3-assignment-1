package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class TaskServiceTest {
    @Autowired
    TaskService taskService;

    @BeforeEach
    void setUp() {
        for (int i = 0; i < 10; i++) {
            Task task = new Task();
            task.setTitle("task" + i);
            taskService.createTask(task);
        }
    }

    @Test
    @DisplayName("할일 조회 리스트 성공 테스트")
    void getTasksSuccess() {
        Assertions.assertThat(taskService.getTasks()).hasSize(10);
    }

    @Test
    @DisplayName("할일 상세 조회 성공 테스트")
    void getTaskSuccess(){
        Assertions.assertThat(taskService.getTask(1L).getTitle()).isEqualTo("task0");
    }

    @Test
    @DisplayName("할일 상세 조회시 할일이 없는 경우")
    void getTaskFail(){
        Assertions.assertThatThrownBy(() -> taskService.getTask(100L)).isInstanceOf(TaskNotFoundException.class);
    }
    
}
