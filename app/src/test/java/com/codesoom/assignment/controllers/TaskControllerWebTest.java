package com.codesoom.assignment.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		TaskService taskService = new TaskService();
		TaskController taskController = new TaskController(taskService);
		TaskErrorAdvice taskErrorAdvice = new TaskErrorAdvice();
		mockMvc = MockMvcBuilders.standaloneSetup(taskController).setControllerAdvice(taskErrorAdvice).build();
	}

	@Test
	@DisplayName("tasks 목록을 받아온다")
	void list() throws Exception {
		mockMvc.perform(get("/tasks")).andExpect(status().isOk());
	}

	@Test
	@DisplayName("유효한 ID의 task 를 받아온다")
	void detailWithValidId() throws Exception {
		create();
		mockMvc.perform(get("/tasks/1")).andExpect(status().isOk()).andDo(print());
	}

	@Test
	@DisplayName("유효하지 않는 ID의 task 를 받아온다")
	void detailWithInValidId() throws Exception {
		mockMvc.perform(get("/tasks/100")).andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	@DisplayName("task 를 생성한다")
	void create() throws Exception {
		Task task = new Task();
		task.setTitle("test");
		String source = objectMapper.writeValueAsString(task);
		mockMvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(source))
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("task 를 변경한다")
	void update() throws Exception {
		create();
		Task task = new Task();
		task.setTitle("update");
		String source = objectMapper.writeValueAsString(task);
		mockMvc.perform(put("/tasks/1").contentType(MediaType.APPLICATION_JSON).content(source))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("task 를 변경한다")
	void patchTest() throws Exception {
		create();
		Task task = new Task();
		task.setTitle("update");
		String source = objectMapper.writeValueAsString(task);
		mockMvc.perform(patch("/tasks/1").contentType(MediaType.APPLICATION_JSON).content(source))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("task 를 제거한다")
	void deleteTest() throws Exception {
		create();
		mockMvc.perform(delete("/tasks/1").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}
}
