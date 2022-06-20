package com.codesoom.assignment.controllers;

import com.codesoom.assignment.dto.ErrorResponse;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TaskErrorAdviceTest {
    TaskErrorAdvice taskErrorAdvice;
    static final String TASK_NOT_FOUND_ERR_MSG = "Task not found";
    @BeforeEach
    void setup(){
        taskErrorAdvice = new TaskErrorAdvice();
    }

    @Test
    public void handleNotFoundTest() {
        //given
        //when
        ErrorResponse errRes = taskErrorAdvice.handleNotFound();
        //then
        assertThat(errRes.getMessage()).isEqualTo(TASK_NOT_FOUND_ERR_MSG);
    }
}