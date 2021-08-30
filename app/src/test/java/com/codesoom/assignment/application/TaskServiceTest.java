package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService 클래스")
class TaskServiceTest {
    private TaskService taskService;

    private static final long VALID_TEST_ID = 1L;
    private static final long INVALID_TEST_ID = 20000L;
    private static final String TEST_TITLE = "testTitle";
    private static final String PARAM_TEST_TITLE = "paramTestTitle";

    //질문 Java에서 익명클래스의 장황함을 개선하기 위해 람다를 사용하는 이유와 같은 느낌의 장황함을 느낌.
    @BeforeEach
    //fixtures
    void setUp() {
        //subject : 테스트하는 대상
        taskService = new TaskService();

        Task task = new Task();
        task.setTitle(TEST_TITLE);
        taskService.createTask(task);
    }

    private Task makeSource(){
        Task task = new Task();
        task.setTitle(PARAM_TEST_TITLE);
        return task;
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list{
        @Nested
        @DisplayName("만약 클라이언트 호출을 받으면")
        class Context_wiht_an_clinent_call{
            @Test
            @DisplayName("list를 리턴한다.")
            void getTasks() {
                assertThat(taskService.getTasks()).isInstanceOf(List.class);
            }
        }
    }


    @Nested
    @DisplayName("get 메소드는")
    class Describe_get{
        @Nested
        @DisplayName("만약 올바른 id를 전달받았다면")
        class Context_with_VALID_ID_id {
            @Test
            @DisplayName("id에 해당하는 task를 리턴한다.")
            void it_returns_a_task(){
                Task result = taskService.getTask(VALID_TEST_ID);

                assertThat(result.getTitle()).isEqualTo(TEST_TITLE);
                assertThat(result).isInstanceOf(Task.class);
            }
        }

        @Nested
        @DisplayName("만약 올바르지 못한 id를 전달받았다면")
        class Context_with_invalid_id{
            @Test
            @DisplayName("Exception을 발생한다.")
            void it_returns_an_exception(){
                //when(taskService.getTask(INVALID_ID)).thenThrow(TaskNotFoundException.class);
                assertThatThrownBy(() -> taskService.getTask(INVALID_TEST_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create{
        @Nested
        @DisplayName("만약 올바른 값을 전달받았다면")
        class Context_with_valid_param{

            @Test
            @DisplayName("생성된 task를 리턴한다.")
            void it_returns_a_task() {
                Task source = makeSource();
                Task result = taskService.createTask(source);

                assertThat(result).isInstanceOf(Task.class);
                assertThat(result.getId()).isEqualTo(VALID_TEST_ID + 1);
                assertThat(result.getTitle()).isEqualTo(PARAM_TEST_TITLE);
            }
        }

        @Nested
        @DisplayName("만약 올바르지 못한 값을 전달받았다면")
        class Context_with_invlaid_param{

        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update{
        @Nested
        @DisplayName("만약 유효한 id를 전달받았다면")
        class Context_with_valid_param{
            @Test
            @DisplayName("수정한 task를 리턴한다.")
            void it_returns_a_task() {
                Task source = makeSource();
                Task result = taskService.updateTask(VALID_TEST_ID, source);

                assertThat(result).isInstanceOf(Task.class);
                assertThat(result.getId()).isEqualTo(VALID_TEST_ID);
                assertThat(result.getTitle()).isEqualTo(PARAM_TEST_TITLE);
            }
        }

        @Nested
        @DisplayName("만약 유효한 id를 전달받지 못했을 경우")
        class Context_with_invalid_id{
            @Test
            @DisplayName("TaskNotFoundException를 리턴한다.")
            void it_returns_a_task() {
                Task source = makeSource();
                assertThatThrownBy(() -> taskService.updateTask(INVALID_TEST_ID, source))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete{
        @Nested
        @DisplayName("만약 유효한 id를 전달받았다면")
        class Context_with_valid_param{
            @Test
            @DisplayName("수정한 task를 리턴한다.")
            void it_returns_a_task() {
                Task result = taskService.deleteTask(VALID_TEST_ID);

                assertThat(result).isInstanceOf(Task.class);
                assertThat(result.getId()).isEqualTo(VALID_TEST_ID);
            }
        }

        @Nested
        @DisplayName("만약 유효한 id를 전달받지 못했을 경우")
        class Context_with_invalid_id{
            @Test
            @DisplayName("TaskNotFoundException를 리턴한다.")
            void it_returns_a_task() {
                Task source = makeSource();
                assertThatThrownBy(() -> taskService.deleteTask(INVALID_TEST_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}