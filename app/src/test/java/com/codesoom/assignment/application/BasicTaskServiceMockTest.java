package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_get_task {
        @Nested
        @DisplayName("주어진 식별자를 가진 작업이 있다면")
        class Context_with_task {
            @Test
            @DisplayName("작업을 리턴한다")
            void It_returns_task() {
                given(taskRepository.get(givenId)).willReturn(
                        Optional.of(new Task(givenId, givenTitle)));

                assertThat(taskService.getTask(givenId)).isEqualTo(new Task(givenId, givenTitle));

                verify(taskRepository).get(givenId);
            }
        }

        @Nested
        @DisplayName("주어진 식별자를 가진 작업이 없다면")
        class Context_without_task {
            @Test
            @DisplayName("예외를 던집니다")
            void It_throw_exception() {
                given(taskRepository.get(givenId)).willReturn(Optional.empty());

                assertThatThrownBy(() -> taskService.getTask(givenId))
                        .isExactlyInstanceOf(TaskNotFoundException.class);

                verifyNoMoreInteractions(taskRepository);
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_create {
        @Nested
        @DisplayName("제목이 주어지면")
        class Context_with_title {
            @Test
            @DisplayName("식별자를 가진 작업을 리턴한다")
            void It_returns_task() {
                given(taskRepository.add(givenTitle)).willReturn(new Task(givenId, givenTitle));

                assertThat(taskService.createTask(givenTitle)).isEqualTo(new Task(givenId, givenTitle));

                verify(taskRepository).add(anyString());
            }
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_update {
        @Nested
        @DisplayName("식별자와 수정할 제목이 주어지면")
        class Context_with_id_and_title {
            @Test
            @DisplayName("수정한 제목을 가진 작업을 리턴한다")
            void It_returns_taskChangeTitle() {
                given(taskRepository.get(givenId)).willReturn(
                        Optional.of(new Task(givenId, givenTitle)));

                assertThat(taskService.updateTask(givenId, "변경 후"))
                        .isEqualTo(new Task(givenId, "변경 후"));
            }
        }
    }
}
