package com.codesoom.assignment;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TaskController 클래스")
public class ControllerTest extends TestHelper { // FIXME: 이름을 TaskControllerTest로 지으면 java 파일을 인식하지 못한다?!
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private final Task dummyTask1 = dummyTask("1");
    private final Task dummyTask2 = dummyTask("2");
    private final Task dummyTask3 = dummyTask("3");

    @Nested
    @DisplayName("GET /tasks API는")
    class Describe_GET_tasks {

        @Nested
        @DisplayName("등록된 task가 없다면")
        class Context_no_tasks {

            @Test
            @DisplayName("200 OK와 빈 리스트를 리턴한다")
            void it_returns_empty_array() throws Exception {
                given(taskService.getTasks())
                        .willReturn(new ArrayList<>());

                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("[]")));
            }
        }

        @Nested
        @DisplayName("등록된 task가 있다면")
        class Context_multiple_tasks {
            private List<Task> taskList = new ArrayList<>();

            @BeforeEach
            void setUp() {
                taskList.add(dummyTask1);
                taskList.add(dummyTask2);
                taskList.add(dummyTask3);
            }

            @Test
            @DisplayName("200 OK와 task가 포함된 리스트를 반환한다")
            void it_returns_array() throws Exception {
                given(taskService.getTasks())
                        .willReturn(taskList);

                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(dummyTask1.getTitle())))
                        .andExpect(content().string(containsString(dummyTask2.getTitle())))
                        .andExpect(content().string(containsString(dummyTask3.getTitle())));
            }
        }
    }
}
