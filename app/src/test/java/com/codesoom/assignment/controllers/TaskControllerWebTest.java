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
@DisplayName("TaskControllerWeb 클래스는")
public class TaskControllerWebTest {

	@Nested
	class task가_주어질때 {

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
		class tasks_를_입력하면 {
			@Test
			void get_method일때_tasks_리스트를_받아온다() throws Exception {
				create();
				mockMvc.perform(get("/tasks")).andExpect(status().isOk());
			}
		}

		@Nested
		class tasks와_id를_입력하면 {
			@Test
			void 유효한_ID의_get_method일때_task를_받아온다() throws Exception {
				create();
				mockMvc.perform(get("/tasks/1")).andExpect(status().isOk()).andDo(print());
			}
			@Test
			void 유호하지_않는_ID의_get_method일때_NOT_FOUND를_뱉는다() throws Exception {
				mockMvc.perform(get("/tasks/100")).andExpect(status().isNotFound()).andDo(print());
			}
			@Test
			void update_method일때_task_title를_변경한다() throws Exception {
				create();
				Task task = new Task();
				task.setTitle("update");
				String source = objectMapper.writeValueAsString(task);
				mockMvc.perform(put("/tasks/1").contentType(MediaType.APPLICATION_JSON).content(source))
					.andExpect(status().isOk());
			}
			@Test
			void patch_method일때_task_title를_변경한다() throws Exception {
				create();
				Task task = new Task();
				task.setTitle("update");
				String source = objectMapper.writeValueAsString(task);
				mockMvc.perform(patch("/tasks/1").contentType(MediaType.APPLICATION_JSON).content(source))
					.andExpect(status().isOk());
			}
			@Test
			void delete_method일때_task를_제거한다() throws Exception {
				create();
				mockMvc.perform(delete("/tasks/1").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isNoContent());
			}
		}
	}
}
