package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TaskService 클래스")
class TaskServiceTest {
    private TaskService service;
    @BeforeEach
    void setUp() {
        service = new TaskService();

    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {
        @Nested
        @DisplayName("만약 task가 없다면")
        class No_task{
            @Test
            @DisplayName("빈 리스트를 리턴한다.")
            void it_return_empty_List(){
                assertThat(service.getTasks()).isEqualTo(new ArrayList());
            }

        }
        @Nested
        @DisplayName("만약 task가 3개 있으면")
        class Have_task{
            final static int TASK_SIZE = 3;
            @BeforeEach
            void setup(){
                Task task = new Task();
                for(int i = 0; i < TASK_SIZE; i++){
                    task.setTitle("test title-"+ i);
                    service.createTask(task);
                }
            }
            @Test
            @DisplayName("크기 3의 리스트를 반환한다.")
            void it_retruns_tasks(){
                assertThat(service.getTasks()).hasSize(TASK_SIZE);
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {
        @Nested
        @DisplayName("만약 요청한 Id에 해당하는 task를 찾지 못하면")
        class No_task{
            final Long INVALID_ID = 32340L;
            @Test
            @DisplayName("TasksNotFoundException을 발생시킨다.")
            void it_throws_TaskNotFoundException() {
                assertThatThrownBy(() -> service.getTask(INVALID_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
        @Nested
        @DisplayName("만약 요청한 Id에 해당하는 task를 찾으면")
        class has_task{
            final Long VALID_ID = 1L;
            static final String TASK_TITLE = "Test Title";
            @BeforeEach
            void setup(){
                Task task = new Task();
                task.setTitle(TASK_TITLE);
                service.createTask(task);
            }

            @Test
            @DisplayName("찾은 task를 반환한다.")
            void return_found_task() {
                assertThat(service.getTask(VALID_ID).getId()).isEqualTo(VALID_ID);
                assertThat(service.getTask(VALID_ID).getTitle()).isEqualTo(TASK_TITLE);
            }
        }

    }

    @Test
    void createTask() {

    }

    @Test
    void updateTask() {
    }

    @Test
    void deleteTask() {
    }
}