package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
@DisplayName("TaskControllerWebTest")
class TaskControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    private TaskController taskController;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);
    }

    @Test
    @DisplayName("목록은 처음에는 빈 목록이어야한다.")
    void list() throws Exception {
        mockMvc.perform(get("/tasks")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("에러 발생.")
    void list2() throws Exception {
        mockMvc.perform(get("/tasks/100")).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("task를 만들었다면, 더이상 빈 목록은 아니게 된다.")
    void createNewTest() throws Exception {
        mockMvc.perform(
                post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"hello\"}")
        )
                .andExpect(status().isCreated());
    }


    @Nested
    @DisplayName("list 메소드")
    class method_of_list {

        @Nested
        @DisplayName("if tasks isEmpty")
        class if_tasks_isEmpty {
            @Test
            @DisplayName("return []")
            void return_empty_list(){
                List<Task> tasks = taskService.getTasks();
                assertThat(tasks).isEmpty();
            }
        }
        @Nested
        @DisplayName("if tasks is not Empty")
        class if_tasks_isNotEmpty {
            @Test
            @DisplayName("return tasks")
            void it_returns_all_tasks() {
                Task task1 = generateTask(1L, "task1");
                Task task2 = generateTask(2L, "task2");

                taskService.createTask(task1);
                taskService.createTask(task2);
                List<Task> tasks = taskService.getTasks();
                // id값이 달라져서 다르다고 나와서 해당 사항을 이렇게 테스트했습니다.
                assertThat(tasks.get(0).getTitle()).isEqualTo(task1.getTitle());
                assertThat(tasks.get(1).getTitle()).isEqualTo(task2.getTitle());
                assertThat(tasks.size()).isEqualTo(2);
            }
        }

    }

    private Task generateTask(long id, String title) {
        Task newTask = new Task();
        newTask.setId(id);
        newTask.setTitle(title);
        return newTask;
    }
}
