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

	private final String TASK_TITLE = "NEW Task";
	private final String TASK_UPDATE_TITLE = "UPDATED Task";

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
	}

	@Test
	public void list() throws Exception {
		mockMvc.perform(get("/tasks"))
				.andExpect(status().isOk())
				.andExpect(content().string("[]"));

	}

	@Test
	public void detail() throws Exception {
		assertTaskNotFound(99L);
	}


	@Test
	public void createTask() throws Exception {
		Task task = Task.title("JUnit5 Test Tak");

		assertTaskCreated(task);
	}

	@Test
	public void scenarioTest() throws Exception {
		final long BASIC_TASK_ID = 1L;
		Task newTask = Task.title(TASK_TITLE);
		Task updateTask = Task.title(TASK_UPDATE_TITLE);

		assertTaskNotFound(BASIC_TASK_ID);

		assertTaskCreated(newTask);

		assertTaskDetailContent(BASIC_TASK_ID, TASK_TITLE);

		assertTaskUpdate(BASIC_TASK_ID, updateTask);

		assertTaskDelete(BASIC_TASK_ID);

		assertTaskNotFound(BASIC_TASK_ID);

	}

	private void assertTaskCreated(Task task) throws Exception {
		mockMvc.perform(post("/tasks")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(task)))
				.andExpect(status().isCreated())
				.andExpect(content().string(containsString(task.getTitle())));
	}

	private void assertTaskNotFound(long id) throws Exception {
		mockMvc.perform(get("/tasks/" + id))
				.andExpect(status().isNotFound());
	}

	private void assertTaskDetailContent(long id, String title) throws Exception {
		mockMvc.perform(get("/tasks/" + id))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(title)));
	}

	private void assertTaskUpdate(long id, Task task) throws Exception {
		mockMvc.perform(put("/tasks/" + id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(task)))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(task.getTitle())));
	}

	private void assertTaskDelete(long id) throws Exception {
		mockMvc.perform(delete("/tasks/" + id))
				.andExpect(status().isNoContent());
	}

}
