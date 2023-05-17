package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {

	TaskService taskService;

	@BeforeEach
	void setUp() {
		taskService = new TaskService();

		Task task = Task.title("NEW TASK");
		taskService.createTask(task);
	}

	@Test
	void getTasks() {
		assertThat(taskService.getTasks()).hasSize(1);
	}

	@Test
	void getTaskWithValidId() {
		long id = 1L;

		assertThat(taskService.getTask(id)).isNotNull();
	}

	@Test
	void getTaskWithInvalidId() {
		long id = 99L;

		assertThatThrownBy(() -> taskService.getTask(id)).isInstanceOf(TaskNotFoundException.class);
	}

	@Test
	void createTask() {
		String addTaskTitle = "ADD TASK";
		Task task = Task.title(addTaskTitle);
		int before = taskService.getTasks().size();

		Task created = taskService.createTask(task);
		int after = taskService.getTasks().size();

		assertThat(created.getTitle()).isEqualTo(addTaskTitle);
		assertThat(after - before).isEqualTo(1);
	}

	@Test
	void updateTask() {
		String updateTitle = "UPDATED TASK";
		long id = 1L;
		Task updateTask = Task.title(updateTitle);

		Task task = taskService.updateTask(id, updateTask);

		assertThat(task.getTitle()).isEqualTo(updateTitle);
	}

	@Test
	void deleteTask() {
		long id = 1L;

		taskService.deleteTask(id);

		assertThatThrownBy(() -> taskService.getTask(id)).isInstanceOf(TaskNotFoundException.class);
	}

}