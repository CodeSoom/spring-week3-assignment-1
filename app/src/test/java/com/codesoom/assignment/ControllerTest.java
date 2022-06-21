package com.codesoom.assignment;

import com.codesoom.assignment.application.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TaskController 클래스")
public class ControllerTest { // FIXME: 이름을 TaskControllerTest로 지으면 java 파일을 인식하지 못한다?!
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Nested
    @DisplayName("GET /tasks API는")
    class Describe_GET_tasks {

        @Nested
        @DisplayName("등록된 task가 없다면")
        class Context_no_tasks {

            @Test
            @DisplayName("빈 리스트를 리턴한다")
            void it_returns_empty_array() throws Exception {
                given(taskService.getTasks())
                        .willThrow(new TaskNotFoundException(Long.MAX_VALUE))
                        .willReturn(new ArrayList<>());

                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isNotFound());
            }
        }
    }
}
