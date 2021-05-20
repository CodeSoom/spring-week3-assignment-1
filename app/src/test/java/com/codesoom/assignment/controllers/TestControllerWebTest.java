package com.codesoom.assignment.controllers;

import com.codesoom.assignment.common.exceptions.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TestControllerWebTest {

    @MockBean
    private TaskService taskService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private final Long NOT_FOUND_TASK_ID = 100L; // 목록에 없는 할 일 ID
    private final Long NEW_TASK_ID = 1L; // 새로 생성할 할 일 ID

    private final String NEW_TASK_TITLE = "Test Title"; // 새로 생성할 할 일 제목
    private final String UPDATE_TASK_TITLE = "Test Title Updated"; // 수정된 할 일 제목
    private final String TASK_NOT_FOUND_ERROR_MESSAGE = "해당하는 Task가 존재하지 않습니다.";

    @Test
    @DisplayName("빈 할 일 목록을 조회합니다.")
    void listEmpty() throws Exception {

        //given
        List<Task> tasks = new ArrayList<>();
        given(taskService.getTaskList()).willReturn(tasks);

        //when
        //then
        mockMvc.perform(get("/tasks"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[]"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("비어있지 않은 할 일 목록을 조회합니다.")
    void list() throws Exception {

        //given
        List<Task> tasks = new ArrayList<>();
        Task newTask = new Task();
        newTask.setId(NEW_TASK_ID);
        newTask.setTitle(NEW_TASK_TITLE);
        tasks.add(newTask);

        given(taskService.getTaskList()).willReturn(tasks);

        //when
        //then
        mockMvc.perform(get("/tasks"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(tasks)))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("할 일 목록에 없는 할 일을 조회 할 때 에러를 확인합니다.")
    void detailWithInvalidId() throws Exception {
        //given
        given(taskService.findTaskOne(NOT_FOUND_TASK_ID)).willThrow(TaskNotFoundException.class);

        //when
        //then
        mockMvc.perform(get("/tasks/"+NOT_FOUND_TASK_ID))
                .andExpect(content().string(TASK_NOT_FOUND_ERROR_MESSAGE))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("할 일 목록에 있는 할 일을 조회합니다.")
    void detailWithValidId() throws Exception {

        //given
        Long findTaskId = NEW_TASK_ID;
        List<Task> tasks = new ArrayList<>();

        Task newTask = new Task();
        newTask.setId(NEW_TASK_ID);
        newTask.setTitle(NEW_TASK_TITLE);
        tasks.add(newTask);

        given(taskService.findTaskOne(findTaskId)).willReturn(newTask);

        //when
        //then
        mockMvc.perform(get("/tasks/"+findTaskId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(NEW_TASK_TITLE)));
    }


    @Test
    @DisplayName("새 할 일을 생성하여 할 일 목록에 저장합니다.")
    void create() throws Exception {

        //given
        Task newTask = new Task();
        newTask.setId(NEW_TASK_ID);
        newTask.setTitle(NEW_TASK_TITLE);

        given(taskService.saveTask(newTask)).willReturn(newTask);

        //when
        //then
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(NEW_TASK_TITLE)));

    }

    @Test
    @DisplayName("할 일 목록에 없는 할 일을 수정 할 때 에러를 확인합니다.")
    void updateInvallid() throws Exception {

        //given
        given(taskService.updateTask(NOT_FOUND_TASK_ID, UPDATE_TASK_TITLE)).willThrow(new TaskNotFoundException());

        Task paramTask = new Task();
        paramTask.setTitle(NEW_TASK_TITLE);

        //when
        //then
        mockMvc.perform(put("/tasks/"+NOT_FOUND_TASK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paramTask)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(TASK_NOT_FOUND_ERROR_MESSAGE));

    }

    @Test
    @DisplayName("할 일 목록에 있는 할 일을 수정합니다.")
    void updateValid() throws Exception {

        //given
        Long updateTaskId = NEW_TASK_ID;

        List<Task> tasks = new ArrayList<>();

        Task newTask = new Task();
        newTask.setId(NEW_TASK_ID);
        newTask.setTitle(NEW_TASK_TITLE);

        tasks.add(newTask);

        Task paramTask = new Task();
        paramTask.setId(updateTaskId);
        paramTask.setTitle(UPDATE_TASK_TITLE);

        given(taskService.updateTask(paramTask.getId(), UPDATE_TASK_TITLE)).willReturn(paramTask);

        //when
        //then
        mockMvc.perform(put("/tasks/"+updateTaskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paramTask)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(paramTask)));

    }

    @Test
    @DisplayName("할 일 목록에 없는 할 일을 삭제 할 때 에러를 확인합니다.")
    void deleteInvalid() throws Exception {

        //given
        given(taskService.removeTask(NOT_FOUND_TASK_ID)).willThrow(new TaskNotFoundException());

        //when
        //then
        mockMvc.perform(delete("/tasks/"+NOT_FOUND_TASK_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().string(TASK_NOT_FOUND_ERROR_MESSAGE));

    }

    @Test
    @DisplayName("주어진 ID에 해당하는 할 일을 할 일 목록에서 삭제합니다. 삭제 완료 시 HTTP 204 코드가 반환됩니다.")
    void deleteValid() throws Exception {

        //given
        Long deleteTaskId = NEW_TASK_ID;

        List<Task> tasks = new ArrayList<>();

        Task newTask = new Task();
        newTask.setId(NEW_TASK_ID);
        newTask.setTitle(NEW_TASK_TITLE);
        tasks.add(newTask);

        given(taskService.removeTask(newTask.getId())).willReturn(newTask);

        //when
        //then
        mockMvc.perform(delete("/tasks/"+deleteTaskId))
                .andExpect(status().isNoContent());

    }

}