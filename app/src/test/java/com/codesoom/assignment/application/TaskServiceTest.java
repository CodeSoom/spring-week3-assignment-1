package com.codesoom.assignment.application;

import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    TaskService taskService;

    Task task = new Task();
    List<Task> tasks = new ArrayList<>();

    @BeforeEach
    void init(){
        //Task 설정
        task.setId(1L);
        task.setTitle("책읽기");

        //Tasks 추가
        tasks.add(task);
    }

    @Test
    void testGetTasks(){
        //given
        given(taskService.getTasks()).willReturn(tasks);

        //when
        List<Task> newTasks = taskService.getTasks();

        //then
        assertThat(tasks.get(0).getTitle()).isEqualTo(newTasks.get(0).getTitle());
    }
}
