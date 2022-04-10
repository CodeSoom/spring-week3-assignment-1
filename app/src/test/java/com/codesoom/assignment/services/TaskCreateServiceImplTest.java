package com.codesoom.assignment.services;

import com.codesoom.assignment.domains.Task;
import com.codesoom.assignment.domains.TaskDto;
import com.codesoom.assignment.exceptions.TaskInvalidFormatException;
import com.codesoom.assignment.repositories.InMemoryTaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TaskCreateServiceImplTest {

    @InjectMocks
    private TaskCreateServiceImpl service;

    @Mock
    private InMemoryTaskRepository repository;

    private static final String TASK_TITLE = "test task title";
    private static final String INVALID_TITLE = "";


    @DisplayName("할 일을 성공적으로 추가한다.")
    @Test
    void createTask() {
        //given
        final TaskDto taskDto = new TaskDto(TASK_TITLE);
        when(repository.save(any(Task.class))).thenReturn(new Task(1L, TASK_TITLE));

        //when
        final Task createdTask = service.addTask(taskDto);

        //then
        assertThat(createdTask.getTitle()).isEqualTo(TASK_TITLE);
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
