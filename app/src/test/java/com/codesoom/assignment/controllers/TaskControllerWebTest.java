package com.codesoom.assignment.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
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
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
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
	class tasks_get_method는 {

		@Test
		void tasks_리스트를_받아온다() throws Exception {
			create();
			mockMvc.perform(get("/tasks")).andExpect(status().isOk());
		}
	}

	@Nested
	class tasks_id_get_method는 {
		@Nested
		class ID가_유효할때 {
			@Test
			void task를_받아온다() throws Exception {
				create();
				mockMvc.perform(get("/tasks/1")).andExpect(status().isOk()).andDo(print());
			}
		}

		@Nested
		class ID가_유효하지_않을때 {
			@Test
			void NOT_FOUND를_뱉는다() throws Exception {
				mockMvc.perform(get("/tasks/100")).andExpect(status().isNotFound()).andDo(print());
			}
		}
	}

	@Nested
	class tasks_id_update_method는 {
		@Test
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
	class tasks_id_patch_method는 {
		@Test
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
	class tasks_id_delete_method는 {
		@Test
		void task를_제거한다() throws Exception {
			create();
			mockMvc.perform(delete("/tasks/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
		}
	}
}

