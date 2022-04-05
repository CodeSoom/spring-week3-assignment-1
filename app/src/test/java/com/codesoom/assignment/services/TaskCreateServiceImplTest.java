package com.codesoom.assignment.services;

import com.codesoom.assignment.domains.Task;
import com.codesoom.assignment.domains.TaskDto;
import com.codesoom.assignment.exceptions.TaskInvalidFormatException;
import com.codesoom.assignment.repositories.InMemoryTaskRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TaskCreateServiceImplTest {

    @InjectMocks
    private TaskCreateServiceImpl service;

    @Mock
    private InMemoryTaskRepositoryImpl repository;

    private static final String TASK_TITLE = "test task title";
    private static final String INVALID_TITLE = "";
    private static final Long FIRST_ID = 1L;


    @DisplayName("할 일을 성공적으로 추가한다.")
    @Test
    void createTask() {
        //given
        final TaskDto taskDto = new TaskDto(TASK_TITLE);

        //when
        given(repository.generateId()).willReturn(FIRST_ID);
        final Task createdTask = service.addTask(taskDto);

        //then
        verify(repository).save(createdTask);
        assertThat(createdTask.getTitle()).isEqualTo(TASK_TITLE);
        assertThat(createdTask.getId()).isEqualTo(FIRST_ID);
    }

    @DisplayName("빈 제목을 입력하면 예외가 발생한다.")
    @Test
    void thrownTaskInvalidException() {
        //given
        final TaskDto invalidTaskDto = new TaskDto(INVALID_TITLE);

        //when
        //then
        assertThrows(TaskInvalidFormatException.class, () -> service.addTask(invalidTaskDto));
    }

}