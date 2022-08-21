package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hibernate.type.IntegerType.ZERO;

class TaskServiceTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private final TaskService service = new TaskService();

    @Nested
    @DisplayName("할 일 전체 조회 메소드는")
    class Describe_GetAll{
        @Nested
        @DisplayName("할 일이 있다면")
        class Context_ExistsTask{

            private String saveContent;

            @BeforeEach
            void setUp() throws JsonProcessingException {
                service.clearTasks();
                List<Task> tasks = new ArrayList<>();
                Task task1 = new Task(1L , "TITLE1");
                Task task2 = new Task(2L , "TITLE2");
                tasks.add(task1);
                tasks.add(task2);
                service.createTask(task1);
                service.createTask(task2);
                saveContent = mapper.writeValueAsString(tasks);
            }

            @Test
            @DisplayName("할 일들을 반환한다")
            void It_GetTasks() throws JsonProcessingException {
                String getContent = mapper.writeValueAsString(service.getTasks());
                assertThat(getContent).isEqualTo(saveContent);
            }
        }

        @Nested
        @DisplayName("할 일이 없다면")
        class Contenxt_NotExistsTask{

            @BeforeEach
            void setUp(){
                service.clearTasks();
            }

            @Test
            @DisplayName("빈 배열을 반환한다")
            void It_EmptyArray(){
                assertThat(service.getTasks()).isEqualTo(Collections.EMPTY_LIST);
            }
        }
    }

    @Nested
    @DisplayName("특정 할 일을 조회하는 메소드는")
    class Describe_GetDetail{

        @Nested
        @DisplayName("{id}가 null이거나 해당하는 할 일이 없다면")
        class Context_InvalidId{

            private final Long invalidId = 1L;
            private final Long nullId = null;

            @BeforeEach
            void setUp() {
                service.clearTasks();
            }

            @Test
            @DisplayName("할 일을 찾지 못 했다는 예외를 던진다")
            void It_ThrowException(){
                assertThatThrownBy(() -> service.getTask(invalidId))
                        .isInstanceOf(TaskNotFoundException.class);
                assertThatThrownBy(() -> service.getTask(nullId))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("{id}에 해당하는 할 일이 있다면")
        class Context_ValidId{

            private final Long validId = 1L;
            private Task newTask;

            @BeforeEach
            void setUp() {
                service.clearTasks();
                newTask = new Task();
                newTask.setTitle("NEW TASK");
            }

            @Test
            @DisplayName("할 일을 반환한다")
            void It_GetObject() throws JsonProcessingException {
                String newContent = mapper.writeValueAsString(service.createTask(newTask));
                String getContent = mapper.writeValueAsString(service.getTask(validId));
                assertThat(getContent).isEqualTo(newContent);
            }
        }
    }

    @Nested
    @DisplayName("할 일을 추가하는 메소드는")
    class Describe_Add{

        @Nested
        @DisplayName("null을 전달하면")
        class Context_Null{

            private final Task nullTask = null;

            @Test
            @DisplayName("null 참조 예외가 발생한다")
            void It_Null(){
                assertThatThrownBy(() -> service.createTask(nullTask))
                        .isInstanceOf(NullPointerException.class);
            }
        }

        @Nested
        @DisplayName("할 일의 제목이 없으면")
        class Context_NullTitle{

            private final Task nullTitleTask = new Task();

            @Test
            @DisplayName("제목은 null로 저장된다")
            void It_Save(){
                Task addTask = service.createTask(nullTitleTask);
                assertThat(addTask.getTitle()).isEqualTo(null);
            }
        }

        @Nested
        @DisplayName("할 일의 제목이 있으면")
        class Context_NotNullTitle{

            private final Task task = new Task();

            @BeforeEach
            void setUp() {
                task.setTitle("NOT NULL");
            }

            @Test
            @DisplayName("{id}는 생성되고 해당 제목으로 저장한다")
            void It_Save(){
                Task createTask = service.createTask(task);
                assertThat(createTask.getTitle()).isEqualTo(task.getTitle());
                assertThat(createTask.getId()).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("할 일을 수정하는 메소드는")
    class Describe_Update{

        @Nested
        @DisplayName("수정할 할 일이 있으면")
        class Context_ExistsObject{

            private final Task beforeTask = new Task();
            private final Task afterTask = new Task();
            private final Long validId = 1L;

            @BeforeEach
            void setUp() {
                service.clearTasks();
                service.createTask(beforeTask);
                afterTask.setTitle("UPDATE TITLE");
            }

            @Test
            @DisplayName("수정한다")
            void It_Update(){
                Task updateTask = service.updateTask(validId , afterTask);
                assertThat(updateTask.getTitle()).isEqualTo(afterTask.getTitle());
            }

            @Nested
            @DisplayName("{id}가 null이거나 해당하는 할 일이 없다면")
            class Context_NullId{

                private final Long nullId = null;
                private final Long invalidId = 1L;

                @BeforeEach
                void setUp() {
                    service.clearTasks();
                    service.createTask(new Task());
                    service.deleteTask(invalidId);
                }

                @Test
                @DisplayName("할 일을 찾지 못 한다는 예외를 던진다")
                void It_ThrowException(){
                    assertThatThrownBy(() -> service.updateTask(nullId , beforeTask))
                            .isInstanceOf(TaskNotFoundException.class);
                    assertThatThrownBy(() -> service.updateTask(invalidId , beforeTask))
                            .isInstanceOf(TaskNotFoundException.class);
                }
            }
        }
    }

    @Nested
    @DisplayName("할 일을 삭제하는 메소드는")
    class Describe_Delete{

        @Nested
        @DisplayName("삭제할 {id}에 해당하는 할 일이 있다면")
        class Context_ValidId{

            private final Long validId = 1L;

            @BeforeEach
            void setUp() {
                service.clearTasks();
                service.createTask(new Task());
                service.createTask(new Task());
            }

            @Test
            @DisplayName("삭제한다")
            void It_Delete(){
                int oldSize = service.getTasks().size();
                service.deleteTask(validId);
                assertThat(service.getTasks()).hasSize(oldSize - 1);
            }
        }

        @Nested
        @DisplayName("{id}가 null이거나 해당하는 할 일이 없다면")
        class Context_NullId{

            private final Long nullId = null;
            private final Long invalidId = 1L;

            @BeforeEach
            void setUp() {
                service.clearTasks();
                service.createTask(new Task());
                service.deleteTask(invalidId);
            }

            @Test
            @DisplayName("할 일을 찾지 못 한다는 예외를 던진다")
            void It_ThrowException(){
                assertThatThrownBy(() -> service.deleteTask(nullId))
                        .isInstanceOf(TaskNotFoundException.class);
                assertThatThrownBy(() -> service.deleteTask(invalidId))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("할 일 초기화 메서드는")
    class Describe_Clear{

        private final int size = 10;
        private final int one = 1;

        @BeforeEach
        void setUp() {
            service.clearTasks();
            for(int i = 0 ; i < size ; i++){
                service.createTask(new Task());
            }
        }

        @Test
        @DisplayName("할 일들을 초기화 한다")
        void It_Clear(){
            service.clearTasks();
            assertThat(service.getTasks()).hasSize(ZERO);
        }

        @Test
        @DisplayName("ID를 초기화 한다")
        void It_IdClear(){
            service.clearTasks();
            assertThat(service.createTask(new Task()).getId()).isEqualTo(one);
        }
    }

    @Nested
    @DisplayName("ID 생성 메소드는")
    class Describe_GenerateId{

        @Test
        @DisplayName("동시성에 취약하다")
        void It_WeakConcurrency(){

        }
    }
}
