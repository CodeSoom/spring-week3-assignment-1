package com.codesoom.assignment.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
@DisplayName("TaskController 클래스는")
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

	void create() throws Exception {
		Task task = new Task();
		task.setTitle("test");
		String source = objectMapper.writeValueAsString(task);
		mockMvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(source))
			.andExpect(status().isCreated());
	}

	@Nested
	@DisplayName("/tasks/ get method 는 ")
	class getTasks {
		@Test
		@DisplayName("tasks 리스트를 받아온다")
		void tasks_리스트를_받아온다() throws Exception {
			create();
			mockMvc.perform(get("/tasks")).andExpect(status().isOk());
		}
	}

	@Nested
	@DisplayName("/tasks/{id} get method 는 ")
	class getTask {
		@Nested
		@DisplayName("ID가 유효할 때")
		class ID가_유효할때 {
			@Test
			@DisplayName("해당 ID의 task 를 반환한다")
			void task를_받아온다() throws Exception {
				create();
				mockMvc.perform(get("/tasks/1")).andExpect(status().isOk()).andDo(print());
			}
		}

		@Nested
		@DisplayName("ID가 유효하지 않을 때")
		class ID가_유효하지_않을때 {
			@Test
			@DisplayName("NOT FOUND 를 반환한다")
			void NOT_FOUND를_뱉는다() throws Exception {
				mockMvc.perform(get("/tasks/100")).andExpect(status().isNotFound()).andDo(print());
			}
		}
	}

	@Nested
	@DisplayName("/tasks/{id} update method 는 ")
	class updateTask {
		@Test
		@DisplayName("task title 를 변경한다")
		void task_title를_변경한다() throws Exception {
			create();
			Task task = new Task();
			task.setTitle("update");
			String source = objectMapper.writeValueAsString(task);
			mockMvc.perform(put("/tasks/1").contentType(MediaType.APPLICATION_JSON).content(source))
				.andExpect(status().isOk());
		}
	}

	@Nested
	@DisplayName("/tasks/{id} patch method 는 ")
	class patchTask {
		@Test
		@DisplayName("title 를 변경한다")
		void task_title를_변경한다() throws Exception {
			create();
			Task task = new Task();
			task.setTitle("update");
			String source = objectMapper.writeValueAsString(task);
			mockMvc.perform(patch("/tasks/1").contentType(MediaType.APPLICATION_JSON).content(source))
				.andExpect(status().isOk());
		}
	}

	@Nested
	@DisplayName("/tasks/{id} delete method 는 ")
	class deleteTask {
		@Test
		@DisplayName("task 를 제거한다")
		void task를_제거한다() throws Exception {
			create();
			mockMvc.perform(delete("/tasks/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
		}
	}
}

