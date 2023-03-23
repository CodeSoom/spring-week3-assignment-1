package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("할일 컨트롤러 기능 테스트 ")
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Test
    @DisplayName("할 일 리스트 조회를 성공하면 200을 반환한다.")
    public void getList_success() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("유효한 할일에 대하여 상세 조회를 성공하면  201을 반환한다.")
    public void detailWithValidId() throws Exception {
        Task task = new Task();
        given(taskService.getTask(any())).willReturn(task);

        System.out.println(taskService.getTask(10L).getTitle());
        mockMvc.perform(get("/tasks/10"))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("유효하지 않은 할일에 대하여 상세 조회를 요청하면  404 에러를 반환한다.")
    public void detailWithInvalidId() throws Exception {
        mockMvc.perform(get("/tasks/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("정상적으로 할 일이 만들어지면 201 상태 코드를 반환한다. ")
    public void createTask() throws Exception {
        Task task = new Task();
        task.setTitle("Hello");
        given(taskService.createTask(any())).willReturn(task);

        mockMvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\" :\"test\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string(("{\"id\":null,\"title\":\"Hello\"}")));
    }

    @Test
    @DisplayName("할 일이 정상적으로 변경되면 200상태코드를 반환하고 해당 할 일을 반환한다.")
    public void update() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("test");

        given(taskService.updateTask(anyLong(), any())).willReturn(task);

        mockMvc.perform(patch("/tasks/1").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\" :\"test\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(("{\"id\":1,\"title\":\"test\"}")));
    }

    @Test
    @DisplayName("존재하지 않은 할 일에 대하여 변경하면 404 에러를 반환한다. ")
    public void updateWithNotFoundException() throws Exception {
        given(taskService.updateTask(anyLong(), any())).willThrow(TaskNotFoundException.class);

        mockMvc.perform(patch("/tasks/1").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\" :\"test\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("정상적으로 할 일을 삭제했을 경우 204코드를 반환한다. ")
    public void deleteTask() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("test");
        given(taskService.deleteTask(anyLong())).willReturn(task);

        mockMvc.perform(delete("/tasks/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("존재하지 않은 할 일을 삭제처리하면 404 에러를 반환한다. ")
    public void test() throws Exception {
        given(taskService.deleteTask(anyLong())).willThrow(TaskNotFoundException.class);

        mockMvc.perform(delete("/tasks/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
