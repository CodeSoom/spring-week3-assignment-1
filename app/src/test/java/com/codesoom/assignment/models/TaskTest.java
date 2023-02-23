package com.codesoom.assignment.models;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@Transactional(readOnly = true)
class TaskTest {

    @Test
    public void domainTest() throws Exception{
        //given
        Task task = new Task();
        task.setTitle("task1");
        //when

        //Then
        assertThat(task.getTitle()).isEqualTo("task1");
    }

}