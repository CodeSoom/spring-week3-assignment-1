package com.codesoom.assignment.controllers;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

	private TaskController taskController;
	private TaskService taskService;

	@BeforeEach
	void setUp() {
		taskService = new TaskService();
		taskController = new TaskController(taskService);
	}

	@Test
	void create() {
		Task task1 = new Task();
		task1.setTitle("test1");

		Task task2 = new Task();
		task2.setTitle("test2");

		taskController.create(task1);
		taskController.create(task2);
	}

	@Test
	void list() {
		create();
		List<Task> taskList =  taskController.list();
		assertThat(taskList).hasSize(2);
	}

	@Test
	void detail() {
		create();
		Task task1 = taskController.detail(1L);
		assertThat(task1.getId()).isEqualTo(1L);
		assertThat(task1.getTitle()).isEqualTo("test1");

		Task task2= taskController.detail(2L);
		assertThat(task2.getId()).isEqualTo(2L);
		assertThat(task2.getTitle()).isEqualTo("test2");
	}

	@Test
	void update() {
		create();
		Task source = new Task();
		source.setTitle("update");

		taskController.update(1L, source);
		Task target = taskController.detail(1L);

		assertThat(target.getTitle()).isEqualTo("update");
	}

	@Test
	void patch() {
		create();
		Task source = new Task();
		source.setTitle("update");

		taskController.update(1L, source);
		Task target = taskController.detail(1L);

		assertThat(target.getTitle()).isEqualTo("update");
	}

	@Test
	void delete() {
		create();
		Task source = new Task();
		source.setTitle("update");

		taskController.delete(1L);

		assertThatThrownBy(() -> taskController.detail(1L))
			.isInstanceOf(TaskNotFoundException.class);
	}
}