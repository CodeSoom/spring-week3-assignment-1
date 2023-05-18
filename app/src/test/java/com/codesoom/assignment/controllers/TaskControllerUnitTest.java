package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TaskControllerUnitTest {

	private TaskController taskController;

	private static final String TEST_TITLE = "TEST1";
	private static final String UPDATE_POSTFIX = "_!!!";


	@BeforeEach
	public void setUp() {
		taskController = new TaskController(new TaskService());

		Task task = new Task();
		task.setTitle(TEST_TITLE);
		taskController.create(task);
	}

	@Test
	public void getList() {
		assertThat(taskController.list()).hasSize(1);
	}

	@Test
	public void getTaskWithInvalidId() {
		Long id = 99L;
		assertThatThrownBy(() -> taskController.detail(id)).isInstanceOf(TaskNotFoundException.class);
	}

	@Test
	public void getTaskWithValidId() {
		assertThat(taskController.detail(1L).getTitle()).isEqualTo(TEST_TITLE);
	}

	@Test
	public void createTask() {
		int before = taskController.list().size();
		taskController.create(new Task());
		int after = taskController.list().size();

		assertThat(after - before).isEqualTo(1);
	}

	@Test
	public void updateWithInvalidId() {
		Long id = 99L;
		Task task = new Task();
		task.setTitle(TEST_TITLE + UPDATE_POSTFIX);

		assertThatThrownBy(() -> taskController.update(id, task)).isInstanceOf(TaskNotFoundException.class);
	}

	@Test
	public void updateWithValidId() {
		Long id = 1L;
		Task task = new Task();
		task.setTitle(TEST_TITLE + UPDATE_POSTFIX);

		Task update = taskController.update(id, task);

		assertThat(update.getTitle()).isEqualTo(TEST_TITLE + UPDATE_POSTFIX);
	}

	@Test
	public void patchWithInvalidId() {
		Long id = 99L;
		Task task = new Task();
		task.setTitle(TEST_TITLE + UPDATE_POSTFIX);

		assertThatThrownBy(() -> taskController.patch(id, task)).isInstanceOf(TaskNotFoundException.class);
	}

	@Test
	public void patchWithValidId() {
		Long id = 1L;
		Task task = new Task();
		task.setTitle(TEST_TITLE + UPDATE_POSTFIX);

		Task update = taskController.patch(id, task);

		assertThat(update.getTitle()).isEqualTo(TEST_TITLE + UPDATE_POSTFIX);
	}

	@Test
	public void deleteWithInvalidId() {
		Long id = 99L;

		assertThatThrownBy(() -> taskController.delete(id)).isInstanceOf(TaskNotFoundException.class);
	}

	@Test
	public void deleteWithValidId() {
		Long id = 1L;

		int before = taskController.list().size();
		taskController.delete(id);
		int after = taskController.list().size();

		assertThat(before - after).isEqualTo(1);
		assertThatThrownBy(() -> taskController.detail(id)).isInstanceOf(TaskNotFoundException.class);
	}

}
