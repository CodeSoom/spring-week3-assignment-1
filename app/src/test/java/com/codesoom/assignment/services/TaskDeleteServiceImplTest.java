package com.codesoom.assignment.services;

import com.codesoom.assignment.domains.Task;
import com.codesoom.assignment.exceptions.TaskNotFoundException;
import com.codesoom.assignment.repositories.InMemoryTaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TaskDeleteServiceImplTest {

    @InjectMocks
    private TaskDeleteServiceImpl service;

    @Mock
    private InMemoryTaskRepository repository;

    private static final Long DELETE_TASK_ID = 1L;
    private static final Task TASK = new Task(DELETE_TASK_ID, "first title");

    @DisplayName("아이디로 할 일을 성공적으로 삭제한다.")
    @Test
    void deleteTask() {
        //given
        given(repository.findById(DELETE_TASK_ID)).willReturn(TASK);

        //when
        service.deleteTaskById(DELETE_TASK_ID);

        //then
        verify(repository).remove(DELETE_TASK_ID);
    }

    @DisplayName("존재하지 않는 할 일을 삭제하면 예외가 발생한다.")
    @Test
    void thrownTaskNotFound() {
        //given
        given(repository.findById(DELETE_TASK_ID)).willReturn(null);

        //when
        //then
        assertThrows(TaskNotFoundException.class, () -> service.deleteTaskById(DELETE_TASK_ID));
    }

}
