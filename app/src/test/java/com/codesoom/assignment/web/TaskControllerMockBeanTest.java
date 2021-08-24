package com.codesoom.assignment.web;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerMockBeanTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    private String title;
    private String updateTitle;
    private Long id;
    private Long notExistsId;
    private Task source;
    private Task updateSource;

    @BeforeEach
    void setup() {
        id = 1L;
        notExistsId = 100L;
        title = "할 일";
        updateTitle = "수정된 할 일";
        source = new Task(title);
        updateSource = new Task(updateTitle);
    }

    @Test
    @DisplayName("할 일 항목 가져오기")
    void getTaskList() throws Exception {
        // given
        List<Task> tasks = new ArrayList<>();
        Task task = new Task(id, title);
        tasks.add(task);
        given(taskService.getTasks()).willReturn(tasks);

        // when
        mockMvc.perform(get("/tasks"))

        // then
        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("할 일 생성")
    void createTask() throws Exception {
        // given
        Task task = new Task(id, title);
        given(taskService.createTask(source)).willReturn(task);

        // when
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(source)))

        // then
        .andExpect(status().isCreated())
        .andExpect(jsonPath("title").value(title));
    }

    @Test
    @DisplayName("할 일 가져오기")
    void getTask() throws Exception {
        // given
        Task task = new Task(id, title);
        given(taskService.getTask(id)).willReturn(task);

        // when
        mockMvc.perform(get("/tasks/" + id))

        // then
        .andExpect(status().isOk())
        .andExpect(jsonPath("id").exists())
        .andExpect(jsonPath("title").value(title));
    }

    @Test
    @DisplayName("할 일 가져오기 - 존재하지 않을 경우")
    void getNotExistsTask() throws Exception {
        // given
        given(taskService.getTask(notExistsId)).willThrow(TaskNotFoundException.class);

        // when
        mockMvc.perform(get("/tasks/" + notExistsId))

        // then
        .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("할 일 수정")
    void modifyTask() throws Exception {
        // given
        given(taskService.updateTask(id, updateSource)).willReturn(updateSource);

        // when
        mockMvc.perform(put("/tasks/" + id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateSource)))

        // then
        .andExpect(status().isOk())
        .andExpect(jsonPath("title").value(updateTitle));
    }

    @Test
    @DisplayName("할 일 삭제")
    void deleteTask() throws Exception {
        // given
        given(taskService.deleteTask(id)).willReturn(source);

        // when
        mockMvc.perform(delete("/tasks/" + id))

        // then
        .andExpect(status().isNoContent());
    }
}
