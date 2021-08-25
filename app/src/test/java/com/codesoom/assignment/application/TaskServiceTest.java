package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class TaskServiceTest {

    @Autowired
    TaskService taskService;
    private String TITLE = "Test1";
    private String UPDATED_TITLE = "Test1_updated";

    @Nested
    @DisplayName("TEST A : 할일 목록이 비었을 경우")
    class testA{
        @BeforeEach
        @DisplayName("TEST A Setting")
        void setUp(){
            //Autowird된 taskService를 초기화할 방법을 찾는 중..
            //아래 코드는 초기화가 아닌 별도의 초기화된 인스턴스로 대체하고 있다.
            taskService = new TaskService();
        }
        @Test
        @DisplayName("할일 목록 가져오기")
        void getTasks(){
            assertThat(taskService.getTasks()).isEmpty();
        }

        @Test
        @DisplayName("1번 할일 가져오기")
        void getTask(){
            assertThatThrownBy(() -> taskService.getTask(1L)).isInstanceOf(TaskNotFoundException.class);
        }

        @Test
        @DisplayName("할일 생성하기")
        void createTask(){
            Task source = new Task();
            source.setTitle(TITLE);
            assertThat(taskService.createTask(source)).isNotNull();
            assertThat(taskService.getTasks()).isNotEmpty()
                                                .contains(source);

            Task source2 = new Task();
            source2.setTitle(TITLE);
            assertThat(taskService.createTask(source2)).isNotNull();
            assertThat(taskService.getTasks()).isNotEmpty()
                                                .contains(source, source2);
        }

        @Test
        @DisplayName("할일 수정하기")
        void updateTask(){
            Task source = new Task();
            source.setTitle(UPDATED_TITLE);
            assertThatThrownBy(() -> taskService.updateTask(1L, source)).isInstanceOf(TaskNotFoundException.class);
        }

        @Test
        @DisplayName("할일 삭제하기")
        void deleteTask(){
            assertThatThrownBy(() -> taskService.deleteTask(1L)).isInstanceOf(TaskNotFoundException.class);
        }


    }

    @Nested
    @DisplayName("TEST B : 할일 목록이 존재할 경우")
    class testB{
        Task source = new Task();

        @BeforeEach
        @DisplayName("TEST B Setting")
        void setUp(){
            taskService = new TaskService();
            source.setTitle(TITLE);
            taskService.createTask(source);
        }

        @Test
        @DisplayName("할일 목록 가져오기")
        void getTasks(){
            assertThat(taskService.getTasks()).isNotEmpty()
                                                .hasSizeGreaterThan(0);
            assertThat(taskService.getTasks().get(0).getId()).isEqualTo(1L);
            assertThat(taskService.getTasks().get(0).getTitle()).isEqualTo(TITLE);
        }

        @Test
        @DisplayName("1번 할일 가져오기")
        void getTask(){
            assertThat(taskService.getTask(1L)).isNotNull();
            assertThat(taskService.getTask(1l).getId()).isEqualTo(1L);
            assertThat(taskService.getTask(1l).getTitle()).isEqualTo(TITLE);
        }

        @Test
        @DisplayName("할일 생성하기")
        void createTask(){
            Task source2 = new Task();
            source2.setTitle(TITLE);
            assertThat(taskService.createTask(source2)).isNotNull();
            assertThat(taskService.getTasks()).isNotEmpty()
                                                .contains(source, source2);
        }

        @Test
        @DisplayName("할일 수정하기")
        void updateTask(){
            Task source = new Task();
            source.setTitle(UPDATED_TITLE);
            assertThat(taskService.updateTask(1L, source)).isNotNull();
            assertThat(taskService.updateTask(1L, source).getTitle()).isEqualTo(UPDATED_TITLE);
        }

        @Test
        @DisplayName("할일 삭제하기")
        void deleteTask(){
            assertThat(taskService.deleteTask(1L)).isNotNull();
            assertThat(taskService.getTasks()).isEmpty();
        }
    }

}
