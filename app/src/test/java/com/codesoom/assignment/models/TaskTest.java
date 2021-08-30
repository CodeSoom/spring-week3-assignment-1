package com.codesoom.assignment.models;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TaskTest {

  private static final Long TASK_ID = 1L;
  private static final String TASK_TITLE = "TEST";

  private Task task;

  @BeforeEach
  void setUp() {
    task = new Task();
  }


  @Test
  void Test_SetId() {
    task.setId(TASK_ID);
    assertThat(task.getId()).isEqualTo(TASK_ID);
  }


  @Test
  void Test_SetTitle() {
    task.setTitle(TASK_TITLE);
    assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
  }
}
