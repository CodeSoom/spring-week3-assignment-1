package com.codesoom.assignment;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.controllers.TaskController;
import org.junit.jupiter.api.BeforeEach;

public class ControllerTest { // FIXME: 이름을 TaskControllerTest로 지으면 java 파일을 인식하지 못한다?!

    private TaskController taskController;

    @BeforeEach
    private void setUp() {
        TaskService taskService = new TaskService();
        this.taskController = new TaskController(taskService);
    }

    // FIXME:
    //  TaskController에서는 TaskService를 호출하기만 한다.
    //  TaskService의 로직은 TaskServiceTest에서 테스트했다.
    //  TaskController에서는 무엇을 테스트하면 좋을까?
}
