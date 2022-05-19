package com.codesoom.assignment.controller;

import com.codesoom.assignment.domain.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TaskController Web Test")
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String BASE_URI = "/tasks/";
    private final Long ID = 1L;
    private final Long NOT_EXIST_ID = 100L;
    private final String TITLE = "test";

    @Nested
    @DisplayName("GET /tasks")
    class list {

        @Nested
        @DisplayName("할일 목록이 존재하면")
        class when_list_is_not_empty {

            @Test
            @DisplayName("할일 목록과 200 OK를 반환합니다.")
            void list() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk());
            }
        }
    }

    @Nested
    @DisplayName("GET /tasks/{id}")
    class detail {

        @Nested
        @DisplayName("할일이 존재하면")
        class when_task_is_exist {

            @BeforeEach
            void setUp() throws Exception {
                ObjectMapper objectMapper = new ObjectMapper();
                String content = objectMapper.writeValueAsString(new Task(ID, TITLE));

                // mock를 통해 post를 일으키고 테스트 하는 것과
                mockMvc.perform(post(BASE_URI + ID)
                        .content(content));

                // mockbean을 이용하여 TaskService를 만들어서 task를 생성한 후에 다음 테스트를 진행하는 것중 어떤 방식이 나을까요?
            }

            @Test
            @DisplayName("200 OK를 반환합니다.")
            void list() throws Exception {
                mockMvc.perform(get(BASE_URI + ID))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("할일이 존재하지 않으면")
        class when_task_is_not_exist {

            @Test
            @DisplayName("할일과 404 Not Found를 반환합니다.")
            void detail() throws Exception {
                mockMvc.perform(get(BASE_URI + NOT_EXIST_ID))
                        .andExpect(status().isNotFound());
            }
        }
    }
}
