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
	private static final String TEST_TASK_1_NAME = "test1";
	private static final String TEST_TASK_2_NAME = "test2";
	private static final String TEST_TASK_UPDATE_NAME = "update";

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
		task1.setTitle(TEST_TASK_1_NAME);

		Task task2 = new Task();
		task2.setTitle(TEST_TASK_2_NAME);

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
		assertThat(task1.getTitle()).isEqualTo(TEST_TASK_1_NAME);

		Task task2= taskController.detail(2L);
		assertThat(task2.getId()).isEqualTo(2L);
		assertThat(task2.getTitle()).isEqualTo(TEST_TASK_2_NAME);
	}

	@Test
	void detailWithInvalidId() {
		assertThatThrownBy(()->taskController.detail(1L))
			.isInstanceOf(TaskNotFoundException.class);
	}

	@Test
	void detailWithValidId() {
		create();
		Task task1 = taskController.detail(1L);
		assertThat(task1.getId()).isEqualTo(1L);
		assertThat(task1.getTitle()).isEqualTo(TEST_TASK_1_NAME);

		Task task2= taskController.detail(2L);
		assertThat(task2.getId()).isEqualTo(2L);
		assertThat(task2.getTitle()).isEqualTo(TEST_TASK_2_NAME);
	}

	@Test
	void update() {
		create();
		Task source = new Task();
		source.setTitle(TEST_TASK_UPDATE_NAME);

		taskController.update(1L, source);
		Task target = taskController.detail(1L);

		assertThat(target.getTitle()).isEqualTo(TEST_TASK_UPDATE_NAME);
	}

	@Test
	void patch() {
		create();
		Task source = new Task();
		source.setTitle(TEST_TASK_UPDATE_NAME);

		taskController.update(1L, source);
		Task target = taskController.detail(1L);

		assertThat(target.getTitle()).isEqualTo(TEST_TASK_UPDATE_NAME);
	}

	@Test
	void delete() {
		create();
		Task source = new Task();
		source.setTitle(TEST_TASK_1_NAME);

		taskController.delete(1L);

		assertThatThrownBy(() -> taskController.detail(1L))
			.isInstanceOf(TaskNotFoundException.class);
	}
}