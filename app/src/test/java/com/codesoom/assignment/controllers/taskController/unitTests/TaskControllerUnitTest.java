package com.codesoom.assignment.controllers.taskController.unitTests;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.controllers.TaskController;
import com.codesoom.assignment.controllers.taskController.TaskControllerTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class TaskControllerUnitTest extends TaskControllerTest {
    @Mock
    protected TaskService taskServiceMock;

    @InjectMocks
    protected TaskController taskController;
}
