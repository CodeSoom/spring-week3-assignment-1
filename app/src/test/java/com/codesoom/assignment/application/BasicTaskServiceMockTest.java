package com.codesoom.assignment.application;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskService 인터페이스의")
public class BasicTaskServiceMockTest {
    @Mock
    private TaskRepository taskRepository;

    private TaskService taskService;
    private final Long givenId = 0L;
    private final String givenTitle = "Todo";

    @BeforeEach
    void setUp() {
        taskService = new BasicTaskService(taskRepository);
        assertThat(taskService).isNotNull();
        reset(taskRepository);
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_get_tasks {
        @Nested
        @DisplayName("작업들이 주어지면")
        class Context_with_tasks {
            @Test
            @DisplayName("작업들을 리턴한다")
            void It_returns_tasks() {
                Map<Long, Task> taskMap = new HashMap<>();
                taskMap.put(givenId, new Task(givenId, givenTitle));
                taskMap.put(givenId + 1, new Task(givenId + 1, givenTitle));

                given(taskRepository.getAll()).willReturn(taskMap.values());

                assertThat(taskService.getTasks()).hasSize(2);

                verify(taskRepository, times(1)).getAll();
            }
        }

        @Nested
        @DisplayName("작업들이 없다면")
        class Context_without_tasks {
            @Test
            @DisplayName("빈 작업을 리턴한다")
            void It_returns_empty_tasks() {
                HashMap<Long, Task> map = new HashMap<>();
                given(taskRepository.getAll()).willReturn(map.values());

                assertThat(taskService.getTasks()).isEmpty();

                verify(taskRepository).getAll();
            }
        }
    }
}
