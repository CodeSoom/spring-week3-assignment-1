package com.codesoom.assignment.controllers;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @SpringBootTest
 * @AutoConfigureMockMvc
 * full Spring application context is started but without the server.
 */
@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerMockTest {

	@Autowired
	private MockMvc mockMvc;

	ObjectMapper objectMapper;

	private final long VALID_ID = 1L;
	private final long INVALID_ID = 99L;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
	}

	@Test
	public void list() throws Exception {
		mockMvc.perform(get("/tasks"))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void detail() throws Exception {
		mockMvc.perform(get("/tasks/" + VALID_ID))
				.andExpect(status().isNotFound());
	}


	@Test
	public void createTask() throws Exception {
		Task task = new Task();
		task.setTitle("NEW Task");

		mockMvc.perform(post("/tasks")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(task)))
				.andExpect(status().isCreated())
				.andExpect(content().string(containsString("NEW Task")))
				.andDo(print());
	}

	@Test
	public void putTask() throws Exception {
		Task task = new Task();
		task.setTitle("_UPDATE");

		mockMvc.perform(put("/tasks/" + VALID_ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(task)))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void scenarioTest() throws Exception {
		Task task = new Task();
		task.setTitle("NEW Task");

		mockMvc.perform(get("/tasks/1"))
				.andExpect(status().isNotFound());

		mockMvc.perform(post("/tasks")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(task)))
				.andExpect(status().isCreated())
				.andExpect(content().string(containsString("NEW Task")));

		mockMvc.perform(get("/tasks/1"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("NEW Task")));

		Task update = new Task();
		update.setTitle("_UPDATE");

		mockMvc.perform(put("/tasks/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(update)))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("_UPDATE")));

		mockMvc.perform(delete("/tasks/1"))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/tasks/1"))
				.andExpect(status().isNotFound());

	}

}