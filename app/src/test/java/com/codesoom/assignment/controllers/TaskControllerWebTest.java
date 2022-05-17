package com.codesoom.assignment.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@Test
	void list() throws Exception {
		mockMvc.perform(get("/tasks")).andExpect(status().isOk());
	}

	@Test
	void detailWithValidId() throws Exception {
		create();
		mockMvc.perform(get("/tasks/1")).andExpect(status().isOk());
	}

	@Test
	void detailWithInValidId() throws Exception {
		create();
		mockMvc.perform(get("/tasks/1")).andExpect(status().isOk());
	}

	@Test
	void create() throws Exception {
		Task task = new Task();
		task.setTitle("test");
		String source = objectMapper.writeValueAsString(task);
		mockMvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(source))
			.andExpect(status().isCreated());
	}

	@Test
	void update() throws Exception {
		create();
		Task task = new Task();
		task.setTitle("update");
		String source = objectMapper.writeValueAsString(task);
		mockMvc.perform(put("/tasks/1").contentType(MediaType.APPLICATION_JSON).content(source))
			.andExpect(status().isOk());
	}

	@Test
	void patchTest() throws Exception {
		create();
		Task task = new Task();
		task.setTitle("update");
		String source = objectMapper.writeValueAsString(task);
		mockMvc.perform(patch("/tasks/1").contentType(MediaType.APPLICATION_JSON).content(source))
			.andExpect(status().isOk());
	}

	@Test
	void deleteTest() throws Exception {
		create();
		mockMvc.perform(delete("/tasks/1").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}
}
