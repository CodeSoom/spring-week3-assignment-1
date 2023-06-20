package com.codesoom.assignment.models;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

	@Test
	public void taskSetIdTest() {
		long id = 100L;
		String title = "NEW TASK";

		Task task = Task.title(title);
		task.setId(id);

		assertThat(task.getId()).isEqualTo(id);
		assertThat(task.getTitle()).isEqualTo(title);
	}

}
