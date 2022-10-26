package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TaskService_클래스 {
    private TaskService taskService;
    private static final Long TEST_ID = 1L;
    private static final Long TEST_NOT_EXSIT_ID = 100L;
    private static final String TEST_TITLE = "테스트는 재밌군요!";
    private static final String POSTFIX_TITLE = " 그치만 매우 생소하군요!";

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        createBaseTask();
    }

    /**
     * 각 테스트를 위한 fixture 데이터 생성
     */
    void createBaseTask() {
        Task source = new Task();
        source.setTitle(TEST_TITLE);
        Task task = taskService.createTask(source);

        assertThat(task.getId()).isEqualTo(TEST_ID);
        assertThat(task.getTitle()).isEqualTo(TEST_TITLE);
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class getTasks_메서드는 {
        @Test
        @DisplayName("할 일 목록을 반환한다")
        void getTasks() {
            List<Task> tasks = taskService.getTasks();

            // fixture 데이터가 잘 들어오는지 확인
            assertThat(tasks).hasSize(1);
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class getTask_메서드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 찾을_수_있는_id일_경우 {
            @Test
            @DisplayName("해당 id의 할 일을 반환한다")
            void it_returns_task() {
                Task task = taskService.getTask(TEST_ID);

                assertThat(task).isNotNull();
                assertThat(task.getId()).isEqualTo(TEST_ID);
                assertThat(task.getTitle()).isEqualTo(TEST_TITLE);
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 찾을_수_없는_id일_경우 {
            @Test
            @DisplayName("예외를 던진다")
            void it_returns_taskNotFoundException() {
                assertThatThrownBy(() -> taskService.getTask(100L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class createTask_메서드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 할_일을_생성할_경우 {
            @Test
            @DisplayName("생성한 할 일을 반환한다")
            void it_return_created_task() {
                Task source = new Task();
                source.setTitle(POSTFIX_TITLE);
                Task task = taskService.createTask(source);

                assertThat(task).isNotNull();
                assertThat(task.getTitle()).isEqualTo(POSTFIX_TITLE);
            }

            @Test
            @DisplayName("할 일이 하나 늘어난다")
            void it_returns_tasks_size() {
                int oldSize = taskService.getTasks().size();

                taskService.createTask(new Task());

                int newSize = taskService.getTasks().size();

                assertThat(newSize - oldSize).isEqualTo(1);
            }
            
            @Test
            @DisplayName("생성한 할 일의 id값은 유니크하다")
            void it_returns_id_count_one() {
                Task source = new Task();
                source.setTitle(TEST_TITLE);
                Task task = taskService.createTask(source);

                Long newId = task.getId();

                long idCount = taskService.getTasks().stream()
                        .map(Task::getId)
                        .filter(id -> id.equals(newId))
                        .count();

                assertThat(idCount).isEqualTo(1);
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class updateTask_메서드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 찾을_수_있는_id일_경우 {
            @Test
            @DisplayName("수정한 할 일을 반환한다")
            void it_returns_updated_task() {
                Task source = taskService.getTask(TEST_ID);
                Long id = source.getId();
                source.setTitle(TEST_TITLE + POSTFIX_TITLE);

                Task task = taskService.updateTask(id, source);

                assertThat(task).isNotNull();
                assertThat(task.getTitle()).isEqualTo(TEST_TITLE + POSTFIX_TITLE);
            }

            @Test
            @DisplayName("할 일의 id값은 수정하지 않는다")
            void it_returns_id() {
                Task source = new Task();
                source.setId(TEST_NOT_EXSIT_ID);
                source.setTitle(TEST_TITLE + POSTFIX_TITLE);

                Task task = taskService.updateTask(TEST_ID, source);

                assertThat(task.getId()).isEqualTo(TEST_ID);
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 찾을_수_없는_id일_경우 {
            @Test
            @DisplayName("예외를 던진다")
            void it_returns_taskNotFoundException() {
                Task source = new Task();
                source.setTitle(TEST_TITLE + POSTFIX_TITLE);

                assertThatThrownBy(() -> taskService.updateTask(TEST_NOT_EXSIT_ID, source))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class deleteTask_메서드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 찾을_수_있는_id일_경우 {
            @Test
            @DisplayName("삭제한 할 일을 반환한다")
            void it_returns_deleted_task() {
                Task task = taskService.deleteTask(TEST_ID);

                assertThat(task).isNotNull();
                assertThat(task.getId()).isEqualTo(TEST_ID);
                assertThat(task.getTitle()).isEqualTo(TEST_TITLE);
            }

            @Test
            @DisplayName("할 일이 하나 줄어든다")
            void it_returns_tasks_size() {
                int oldSize = taskService.getTasks().size();

                taskService.deleteTask(TEST_ID);

                int newSize = taskService.getTasks().size();

                assertThat(newSize - oldSize).isEqualTo(-1);
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 찾을_수_없는_id일_경우 {
            @Test
            @DisplayName("예외를 던진다")
            void it_returns_taskNotFoundException() {
                assertThatThrownBy(() -> taskService.deleteTask(TEST_NOT_EXSIT_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
