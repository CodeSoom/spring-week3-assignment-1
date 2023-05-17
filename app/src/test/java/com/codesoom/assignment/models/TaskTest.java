package com.codesoom.assignment.models;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

	@Test
	public void taskConstructorTest() {
		String title = "NEW TASK";

		Task created = Task.title(title);

		assertThat(created.getTitle()).isEqualTo(title);
	}

	@Test
	public void taskSetIdTest() {
		long id = 100L;
		Task task = Task.title("NEW TASK");

		task.setId(id);

		assertThat(task.getId()).isEqualTo(id);
	}

	@Test
	public void updateTaskTitleTest() {
		String beforeTitle = "NEW TASK";
		String afterTitle = "UPDATED TASK";
		Task task = Task.title(beforeTitle);

		task.setTitle(afterTitle);

		assertThat(task.getTitle()).isEqualTo(afterTitle);
	}

}