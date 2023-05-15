package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @WebMvcTest
 * Web Layer에서만 간단히 테스트가 가능.
 * 전체적인 spring context가 필요하지 않을 때 mockBean을 사용하여 웹 레이어만 테스트 가능
 */

@WebMvcTest
class TaskControllerWebMvcTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TaskService taskService;

	@BeforeEach
	public void setUp() {
		Task task = new Task();
		task.setTitle("TEST");
		when(taskService.getTasks()).thenReturn(Collections.singletonList(task));
	}

	@Test
	void list() throws Exception {
		mockMvc.perform(get("/tasks")).andExpect(status().isOk())
				.andExpect(content().string(containsString("TEST")));
	}





}