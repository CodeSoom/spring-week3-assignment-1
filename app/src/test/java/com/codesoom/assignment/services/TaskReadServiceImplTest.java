package com.codesoom.assignment.services;


import com.codesoom.assignment.domains.Task;
import com.codesoom.assignment.exceptions.TaskNotFoundException;
import com.codesoom.assignment.repositories.InMemoryTaskRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class TaskReadServiceImplTest {

    @InjectMocks
    private TaskReadServiceImpl service;

    @Mock
    private InMemoryTaskRepositoryImpl repository;

    private static final Task FIRST_TASK = new Task(1L, "first title");
    private static final Task SECOND_TASK = new Task(2L, "second title");
    private static final Map<Long, Task> ALL_TASKS = Map.of(FIRST_TASK.getId(), FIRST_TASK,
            SECOND_TASK.getId(), SECOND_TASK);
    private static final Long NOT_EXIST_ID = 100L;

    @DisplayName("모든 할 일을 성공적으로 조회한다.")
    @Test
    void readTasks() {
        //given
        given(repository.getTasks()).willReturn(ALL_TASKS);

        //when
        final List<Task> tasks = service.getTasks();

        //then
        assertThat(tasks.size()).isEqualTo(ALL_TASKS.size());
    }

    @DisplayName("id로 특정 할 일을 성공적으로 조회한다.")
    @Test
    void findById() {
        //given
        given(repository.findById(FIRST_TASK.getId())).willReturn(FIRST_TASK);

        //when
        final Task task = service.findTaskById(FIRST_TASK.getId());

        //then
        assertThat(task).isEqualTo(FIRST_TASK);
    }

    @DisplayName("id와 일치하는 할 일이 없으면 예외가 발생한다.")
    @Test
    void thrownNotFoundException() {
        //given
        given(repository.findById(NOT_EXIST_ID)).willReturn(null);

        //when then
        assertThrows(TaskNotFoundException.class, () -> service.findTaskById(NOT_EXIST_ID));
    }

}
