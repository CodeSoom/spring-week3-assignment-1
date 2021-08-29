package com.codesoom.assignment.controllers.taskController.unitTests;

import com.codesoom.assignment.models.Task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TaskController 클래스")
public class ListTest extends TaskControllerUnitTest {
    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {
        @Nested
        @DisplayName("저장된 Task가 없다면")
        class Context_task_empty {
            @Test
            @DisplayName("비어있는 리스트를 리턴한다.")
            void it_returns_a_empty_list() {
                when(taskServiceMock.getTasks()).thenReturn(tasks);
                assertThat(taskController.list()).isEmpty();
            }
        }

        @Nested
        @DisplayName("저장된 Task가 있다면")
        class Context_task_exist {

            @BeforeEach
            void setUp() {
                tasks.add(task);
                when(taskServiceMock.getTasks()).thenReturn(tasks);
            }

            @Test
            @DisplayName("저장된 Task가 포함된 리스트를 리턴한다.")
            void it_returns_a_list_with_tasks() {
                assertThat(taskController.list())
                    .hasSize(tasks.size())
                    .extracting(Task::getId, Task::getTitle)
                    .containsExactly(tuple(validId, taskTitle));
            }
        }

        @AfterEach
        void after() {
            verify(taskServiceMock, atLeastOnce()).getTasks();
        }
    }
    
}
