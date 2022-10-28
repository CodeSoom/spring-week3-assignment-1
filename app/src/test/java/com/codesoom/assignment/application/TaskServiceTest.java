package com.codesoom.assignment.application;

import com.codesoom.assignment.exceptions.TaskAlreadyExistException;
import com.codesoom.assignment.exceptions.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService 클래스")
class TaskServiceTest {
    private static final Long TEST_ID = 1L;
    private static final Long TEST_NOT_EXSIT_ID = 100L;
    private static final String TEST_TITLE = "테스트는 재밌군요!";
    private static final String POSTFIX_TITLE = " 그치만 매우 생소하군요!";
    private TaskService taskService;
    private Task taskSource;

    /**
     * 각 테스트에서 필요한 fixture 데이터를 생성합니다.
     */
    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskSource = new Task();
        taskSource.setTitle(TEST_TITLE);

        taskService.createTask(taskSource);
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class getTasks_메서드는 {

        @BeforeEach
        void setUp() { // fixture 데이터 리셋
            taskService = new TaskService();
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 할_일이_없을_때 {
            @Test
            @DisplayName("빈 ArrayList를 리턴한다")
            void it_returns_empty_array() {
                List<Task> tasks = taskService.getTasks();

                assertThat(tasks)
                        .isInstanceOf(ArrayList.class)
                        .isEmpty();
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 할_일이_있을_때 {
            @DisplayName("n개의 할 일 목록을 리턴한다")
            @ParameterizedTest(name = "{arguments}개의 할 일 목록을 리턴한다")
            @ValueSource(ints = {1, 77, 1027})
            void it_returns_tasks(int createCount) {

                createTaskUntilCount(createCount);

                List<Task> tasks = taskService.getTasks();

                assertThat(tasks).hasSize(createCount);
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class getTask_메서드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 찾을_수_있는_id가_주어지면 {
            @Test
            @DisplayName("해당 id의 할 일을 리턴한다")
            void it_returns_task() {
                Task task = taskService.getTask(TEST_ID);

                assertThat(task).isNotNull();
                assertThat(task.getId()).isEqualTo(TEST_ID);
                assertThat(task.getTitle()).isEqualTo(TEST_TITLE);
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 찾을_수_없는_id가_주어지면 {
            @Test
            @DisplayName("예외를 던진다")
            void it_returns_taskNotFoundException() {
                assertThatThrownBy(() -> taskService.getTask(TEST_NOT_EXSIT_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class createTask_메서드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 새로운_할_일이_주어지면 {

            @Nested
            @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
            class 중복된_할_일인_경우 {
                @Test
                @DisplayName("예외를 던진다")
                void it_returns_taskAlreadyExistException() {
                    assertThatThrownBy(() -> taskService.createTask(taskSource))
                            .isInstanceOf(TaskAlreadyExistException.class);
                }

                @Test
                @DisplayName("예외 메시지를 \"이미 등록된 할 일입니다\" 라는 문구로 리턴한다")
                void it_returns_exception_message() {
                    assertThatThrownBy(() -> taskService.createTask(taskSource))
                            .hasMessage("이미 등록된 할 일입니다");
                }
            }

            @Nested
            @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
            class 중복된_할_일이_아닌_경우 {

                private Task createTaskSource;

                @BeforeEach
                void setUp() {
                    createTaskSource = new Task();
                    createTaskSource.setTitle(POSTFIX_TITLE);
                }

                @Test
                @DisplayName("할 일을 저장하고 리턴한다")
                void it_return_created_task() {
                    Task task = taskService.createTask(createTaskSource);

                    assertThat(task).isNotNull();
                    assertThat(task.getTitle()).isEqualTo(POSTFIX_TITLE);
                }

                @Test
                @DisplayName("할 일이 하나 늘어난다")
                void it_returns_tasks_size() {
                    int oldSize = taskService.getTasks().size();

                    taskService.createTask(createTaskSource);

                    int newSize = taskService.getTasks().size();

                    assertThat(newSize - oldSize).isEqualTo(1);
                }

                @Test
                @DisplayName("할 일의 id 값은 유니크한 값으로 저장된다")
                void it_returns_id_count_one() {
                    Task task = taskService.createTask(createTaskSource);
                    Long newId = task.getId();

                    List<Task> tasks = taskService.getTasks();

                    long idCount = tasks.stream()
                            .map(Task::getId)
                            .filter(id -> id.equals(newId))
                            .count();

                    assertThat(idCount).isEqualTo(1);
                }
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class null이_주어지면 {
            @Test
            @DisplayName("예외를 던진다")
            void it_returns_NullPointerException() {
                assertThatThrownBy(() -> taskService.createTask(null))
                        .isInstanceOf(NullPointerException.class);
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class updateTask_메서드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 찾을_수_있는_id가_주어지면 {
            @Test
            @DisplayName("할 일을 수정하고 리턴한다")
            void it_returns_updated_task() {
                Task source = taskService.getTask(TEST_ID);
                Long originId = source.getId();
                source.setTitle(TEST_TITLE + POSTFIX_TITLE);

                Task task = taskService.updateTask(originId, source);

                assertThat(task).isNotNull();
                assertThat(task.getTitle()).isEqualTo(TEST_TITLE + POSTFIX_TITLE);
            }

            @Test
            @DisplayName("할 일의 id값은 수정하지 않는다")
            @Disabled("일어나지 않을 상황을 테스트합니다")
            void it_returns_id() {
                taskSource.setId(TEST_NOT_EXSIT_ID);

                Task task = taskService.updateTask(TEST_ID, taskSource);

                assertThat(task.getId()).isEqualTo(TEST_ID);
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 찾을_수_없는_id가_주어지면 {
            @Test
            @DisplayName("예외를 던진다")
            void it_returns_taskNotFoundException() {
                assertThatThrownBy(() -> taskService.updateTask(TEST_NOT_EXSIT_ID, taskSource))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class deleteTask_메서드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 찾을_수_있는_id가_주어지면 {
            @Test
            @DisplayName("할 일을 삭제하고 리턴한다")
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
        class 찾을_수_없는_id가_주어지면 {
            @Test
            @DisplayName("예외를 던진다")
            void it_returns_taskNotFoundException() {
                assertThatThrownBy(() -> taskService.deleteTask(TEST_NOT_EXSIT_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }


    private void createTaskUntilCount(int createCount) {
        Task tempCreateTask = new Task();

        for (int i = 0; i < createCount; i++) {
            tempCreateTask.setTitle(createCount + " " + i);
            taskService.createTask(tempCreateTask);
        }
    }
}
