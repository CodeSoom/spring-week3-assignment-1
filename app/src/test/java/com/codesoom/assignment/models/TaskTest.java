package com.codesoom.assignment.models;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TaskTest {

  @Test
  void createNewTask() {
    Task task = new Task();
    task.setId(1L);
    task.setTitle("Test Task");

    String title = task.getTitle();
    Long id = task.getId();

    assertThat(title).isEqualTo("Test Task");
    assertThat(id).isEqualTo(1L);
  }
}
