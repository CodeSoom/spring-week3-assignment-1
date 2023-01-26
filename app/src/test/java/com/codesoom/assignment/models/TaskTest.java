package com.codesoom.assignment.models;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskTest {

  public static final String TITLE = "Test Task";
  private Task task;

  @BeforeEach
  void setUp() {
    task = new Task();
    task.setId(1L);
    task.setTitle(TITLE);
  }

  @Test
  void getId() {
    assertThat(task.getId()).isEqualTo(1L);
  }

  @Test
  void setId() {
    task.setId(1001L);
    assertThat(task.getId()).isEqualTo(1001L);
  }

  @Test
  void getTitle() {
    assertThat(task.getTitle()).isEqualTo(TITLE);
  }

  @Test
  void setTitle() {
    String newTitle = "New title";
    task.setTitle(newTitle);
    assertThat(task.getTitle()).isEqualTo(newTitle);
  }
}