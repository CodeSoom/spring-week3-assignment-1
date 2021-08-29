package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.controllers.TaskController;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class TaskServiceTest {
    private String TITLE = "Test1";
    private String UPDATED_TITLE = "Test1_updated";

    @Autowired
    TaskService taskService;
    @Autowired
    TaskController controller;

    @Nested
    @DisplayName("할일 목록이 비었을 경우")
    class testA{
        @BeforeEach
        @DisplayName("Setting")
        void setUp(){
            //Autowird된 taskService를 초기화할 방법을 찾는 중..
            //아래 코드는 초기화가 아닌 별도의 초기화된 인스턴스로 대체하고 있다.
            taskService = new TaskService();
        }

        @Test
        @DisplayName("getTasks 현재 저장된 목록 불러오기")
        void getTasks(){
            assertThat(taskService.getTasks()).isEmpty();
        }

        @Test
        @DisplayName("getTask 1번 할일 불러오기 Validation : Not Found")
        void getTask(){
            assertThatThrownBy(() -> taskService.getTask(1L)).isInstanceOf(TaskNotFoundException.class);
        }

        @Test
        @DisplayName("createTask 할일 1개 생성하기 Validation : 생성 후 객체 반환 Not Null Check, 생성 후 객체 조회 Not Empty, contains")
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
        @DisplayName("updateTask 할일 수정하기 Validation : 수정할 객체 Not Found")
        void updateTask(){
            Task source = new Task();
            source.setTitle(UPDATED_TITLE);
            assertThatThrownBy(() -> taskService.updateTask(1L, source)).isInstanceOf(TaskNotFoundException.class);
        }

        @Test
        @DisplayName("deleteTask 할일 삭제하기 validation : 삭제할 객체 Not found")
        void deleteTask(){
            assertThatThrownBy(() -> taskService.deleteTask(1L)).isInstanceOf(TaskNotFoundException.class);
        }


    }

    @Nested
    @DisplayName("할일 목록이 존재할 경우")
    class testB{
        Task source = new Task();

        @BeforeEach
        void setUp(){
            taskService = new TaskService();
            source.setTitle(TITLE);
            taskService.createTask(source);
        }

        @Test
        @DisplayName("getTasks 현재 저장된 목록 불러오기")
        void getTasks(){
            assertThat(taskService.getTasks()).isNotEmpty()
                                                .hasSizeGreaterThan(0);
            assertThat(taskService.getTasks().get(0).getId()).isEqualTo(1L);
            assertThat(taskService.getTasks().get(0).getTitle()).isEqualTo(TITLE);
        }

        @Test
        @DisplayName("getTask 1번 할일 불러오기 Validation : Not Found")
        void getTask(){
            assertThat(taskService.getTask(1L)).isNotNull();
            assertThat(taskService.getTask(1l).getId()).isEqualTo(1L);
            assertThat(taskService.getTask(1l).getTitle()).isEqualTo(TITLE);
        }

        @Test
        @DisplayName("createTask 할일 1개 생성하기 Validation : 생성 후 객체 반환 Not Null Check, 생성 후 객체 조회 Not Empty, contains")
        void createTask(){
            Task source2 = new Task();
            source2.setTitle(TITLE);
            assertThat(taskService.createTask(source2)).isNotNull();
            assertThat(taskService.getTasks()).isNotEmpty()
                                                .contains(source, source2);
        }

        @Test
        @DisplayName("updateTask 할일 수정하기 Validation : 수정할 객체 Not Found, 업데이트 Title isEqualsTo")
        void updateTask(){
            Task source = new Task();
            source.setTitle(UPDATED_TITLE);
            assertThat(taskService.updateTask(1L, source)).isNotNull();
            assertThat(taskService.updateTask(1L, source).getTitle()).isEqualTo(UPDATED_TITLE);
        }

        @Test
        @DisplayName("deleteTask 할일 삭제하기 validation : 삭제할 객체 Not found, 삭제 후 목록 isEmpty")
        void deleteTask(){
            assertThat(taskService.deleteTask(1L)).isNotNull();
            assertThat(taskService.getTasks()).isEmpty();
        }
    }

}
