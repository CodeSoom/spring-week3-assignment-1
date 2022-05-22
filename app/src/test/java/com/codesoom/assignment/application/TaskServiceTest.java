package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TaskServiceTest 클래스는")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TaskServiceTest {
    private static final Long TASK_ID_ONE = 1L;
    private static final Long TASK_ID_TWO = 2L;
    private static final String TASK_TITLE_ONE = "test_One";
    private static final String TASK_TITLE_TWO = "test_Two";

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        Task task = new Task();
        task.setTitle(TASK_TITLE_ONE);
        taskService.createTask(task);
    }

    @Nested
    class getTasks_메서드는 {
        @Nested
        class Task가_있으면 {
            @Test
            void Task의_리스트를_반환한다() {
                final List<Task> list = taskService.getTasks();
                final int SIZE = taskService.getTasks().size();

                assertThat(list).hasSize(SIZE);
            }
        }
    }

    @Nested
    class getTask_메서드는 {
        @Nested
        class 요청한_Task의_id가_있으면 {
            @Test
            void id에_해당하는_Task를_반환한다() {
                assertThat(TASK_TITLE_ONE).isEqualTo(taskService.getTask(TASK_ID_ONE).getTitle());
            }
        }

        @Nested
        class 요청한_Task의_id가_없으면 {
            @Test
            void TaskNotFoundException를_반환한다() {
                final Long UNKNOWN_ID = TASK_ID_ONE + 10;

                assertThatThrownBy(() -> taskService.getTask(UNKNOWN_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    class createTask_메서드는 {
        @Nested
        class 요청한_Task의_id가_없으면 {
            @Test
            void 새로운_Task에_해당_id를_생성한다() {
                Task task = new Task();
                task.setTitle(TASK_TITLE_TWO);
                taskService.createTask(task);

                final Long NEW_ID_SIZE = Long.valueOf(taskService.getTasks().size());
                final String NEW_TITLE = taskService.getTask(NEW_ID_SIZE).getTitle();

                assertThat(NEW_TITLE).isEqualTo(TASK_TITLE_TWO);
            }
        }
    }

    @Nested
    class update_메서드는 {
        @Nested
        class 요청한_Task의_id가_있으면 {
            @Test
            void Task의_title을_수정하고_해당_title을_반환한다() {
                Task source = new Task();
                source.setTitle(TASK_TITLE_TWO);

                final Long NEW_ID = Long.valueOf(taskService.getTasks().size());
                taskService.updateTask(NEW_ID, source);

                Task task = taskService.getTask(NEW_ID);
                assertThat(task.getTitle()).isEqualTo(TASK_TITLE_TWO);
            }
        }

        @Nested
        class 요청한_Task의_id가_없으면 {
            @Test
            void TaskNotFoundException를_반환한다() {
                Task source = new Task();
                source.setTitle(TASK_TITLE_TWO);

                final Long UNKNOWN_ID = TASK_ID_ONE + 10;

                assertThatThrownBy(() -> taskService.updateTask(UNKNOWN_ID, source))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    class delete_메서드는 {
        @Nested
        class 요청한_Task의_id가_있으면 {
            @Test
            void 해당_id의_Task를_삭제한다() {
                final Long OLD_SIZE = Long.valueOf(taskService.getTasks().size());
                taskService.deleteTask(OLD_SIZE);
                final Long NEW_SIZE = Long.valueOf(taskService.getTasks().size());

                assertThat(NEW_SIZE).isEqualTo(OLD_SIZE - 1);
            }
        }
    }
}
