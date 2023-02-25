package com.codesoom.assignment.controllers;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class MockTest {
    private static final String TITLE_NAME="new title";
    @BeforeEach
    void setup(@Mock TaskController taskController  , @Mock TaskService taskService){
        Task task = new Task();
        task.setTitle(TITLE_NAME);

        taskService.createTask(task);
    }

    @Test
    public void createMockTest(@Mock TaskController taskController) throws Exception{
        assertThat(taskController.list()).isNotNull();
    }

    @Test
    public void createTitle(@Mock TaskController taskController) throws Exception{
        System.out.println("taskController = " + taskController.list());
        assertThat(taskController.list()).isEqualTo(TITLE_NAME);
    }

    @Test
    public void mockTest(@Mock TaskController taskController) throws Exception{
        //given
        System.out.println("taskController = " + taskController.getClass());
        //-> mock 객체가 프록시 객체인가??
    }


}
