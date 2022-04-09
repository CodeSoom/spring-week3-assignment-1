package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.contexts.BaseTaskTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName(value = "TaskControllerMockTest 에서")
class TaskControllerMockTest extends BaseTaskTest {

    MockMvc mockMvc;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new TaskController(taskService))
                .setControllerAdvice(TaskErrorAdvice.class)
                .build();
    }

    @Nested
    @DisplayName(value = "list() 매소드는 ")
    class Describe_readTasks {

        @Nested
        @DisplayName("할일목록이 없다면")
        class Context_with_empty_tasks {

            @Test
            @DisplayName("빈 배열을 반환한다.")
            void it_returns_empty_list() throws Exception {

                mockMvc.perform(get("/tasks"))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().string("[]"));
            }
        }

        @Nested
        @DisplayName("할일목록에 할일이 있다면")
        class Context_with_tasks {

            @Test
            @DisplayName("가지고 있는 모든 할일목록을 반환한다.")
            void it_returns_full_list() throws Exception {

                taskService.createTask(generateNewTask(TASK_TITLE_1));
                taskService.createTask(generateNewTask(TASK_TITLE_2));

                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(
                                containsString(getTaskIdJsonString(TASK_ID_1)))
                        )
                        .andExpect(content().string(
                                containsString(getTaskIdJsonString(TASK_ID_2)))
                        );
            }
        }
    }

    @Nested
    @DisplayName("readTask() 매소드는")
    class Describe_readTask {

        @Nested
        @DisplayName("path id 가 1이상일때")
        class Context_with_valid_path {

            @Nested
            @DisplayName("id와 일치하는 값이 있다면")
            class Context_with_matched_task {

                @Test
                @DisplayName("task 내용을 반환한다.")
                void it_returns_single_task() throws Exception {

                    taskService.createTask(generateNewTask(TASK_TITLE_1));

                    mockMvc.perform(get("/tasks/1"))
                            .andExpect(status().isOk())
                            .andExpect(content().string(getTaskJsonString(TASK_ID_1, TASK_TITLE_1)));
                }
            }

            @Nested
            @DisplayName("id와 일치하는 값이 없다면")
            class Context_without_matched_task {
                @Test
                @DisplayName("Exception 을 반환한다.")
                void it_throws_not_found_exception() throws Exception {

                    mockMvc.perform(get("/tasks/1234"))
                            .andExpect(status().isNotFound());
                }
            }
        }

        @Nested
        @DisplayName("path id가 0이하 일때")
        class Context_with_invalid_path {

            @Test
            @DisplayName("예외를 발생시킨다.")
            void it_throws_exception() throws Exception {
                taskService.createTask(generateNewTask(TASK_TITLE_1));

                mockMvc.perform(get("/tasks/0"))
                        .andExpect(status().isNotFound());
            }
        }
    }


    @Nested
    @DisplayName("addTask() 매소드는")
    class Describe_addTask {

        @Nested
        @DisplayName("할일제목이 없거나 공백이 아닌 Task 값이 입력되면")
        class Context_normal_task {

            @Test
            @DisplayName("할일 목록에 신규할일을 추가하고, 추가된 할일을 반환한다.")
            void it_adds_new_task_and_returns_added_task() throws Exception {

                mockMvc.perform(post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getTaskJsonString(any(), TASK_TITLE_1)))
                        .andExpect(status().isCreated())
                        .andExpect(content().string(containsString(getTaskJsonString(TASK_ID_1, TASK_TITLE_1))));
            }
        }

        @Nested
        @DisplayName("할일제목이 없거나 공백인 Task 값이 입력되면")
        class Context_abnormal_task {

            @Test
            @DisplayName("예외를 발생시킨다.")
            void it_throws_exception() throws Exception {

                mockMvc.perform(post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getTaskJsonString(null, "")))
                        .andExpect(status().isBadRequest());
            }
        }
    }


    @Nested
    @DisplayName("editTasks() 매소드는")
    class Describe_editTask {

        @Nested
        @DisplayName("path id가 1이상 이라면")
        class Context_normal_path_id {

            @Nested
            @DisplayName("할일제목이 없거나 공백이 아닌 Task 값이 입력되면")
            class Context_normal_task {

                @Test
                @DisplayName("path id 와 일치하는 task 가 존재하면 > 제목을 수정한 후 > 수정된 task 를 반환한다.")
                void it_returns_edited_task() throws Exception {

                    taskService.createTask(generateNewTask(TASK_TITLE_1));

                    mockMvc.perform(put("/tasks/1")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(getTaskJsonString(0L, TASK_TITLE_2)))
                            .andExpect(status().isOk())
                            .andExpect(content().string(containsString(getTaskJsonString(TASK_ID_1, TASK_TITLE_2))));
                }

                @Test
                @DisplayName("path id 와 일치하는 task 가 존재하지 않으면 > 오류를 반환한다.")
                void it_throws_exception() throws Exception {

                    taskService.createTask(generateNewTask(TASK_TITLE_1));

                    mockMvc.perform(put("/tasks/123")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(getTaskJsonString(0L, TASK_TITLE_1)))
                            .andExpect(status().isNotFound());
                }
            }

            @Nested
            @DisplayName("할일제목이 없거나 공백인 Task 값이 입력되면")
            class Context_abnormal_task {

                @Test
                @DisplayName("예외를 반환한다.")
                void editTaskTitle_throwErrorIfNotValidTitle() throws Exception {

                    taskService.createTask(generateNewTask(TASK_TITLE_1));

                    mockMvc.perform(put("/tasks/1")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(getTaskJsonString(0L, "")))
                            .andExpect(status().isBadRequest());
                }
            }

        }

        @Nested
        @DisplayName("path id가 0이하 이라면")
        class Context_abnormal_path_id {

            @Test
            @DisplayName("예외를 던진다.")
            void it_throws_exception() throws Exception {
                taskService.createTask(generateNewTask(TASK_TITLE_1));

                mockMvc.perform(put("/tasks/0")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getTaskJsonString(0L, TASK_TITLE_1)))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("deleteTask() 매소드는")
    class Describe_deleteTask {

        @Nested
        @DisplayName("path id가 1이상 이라면")
        class Context_valid_path_id {

            @Nested
            @DisplayName("path id 와 일치하는 task를 찾을 수 있을때")
            class Context_has_matched_task {

                @Test
                @DisplayName("id에 맞는 할일 조회 후 삭제한다.")
                void it_returns_204() throws Exception {

                    taskService.createTask(generateNewTask(TASK_TITLE_1));

                    mockMvc.perform(delete("/tasks/1"))
                                    .andExpect(status().isNoContent());
                }
            }

            @Nested
            @DisplayName("path id 와 일치하는 task를 찾을 수 없을때")
            class Context_no_matched_task {

                @Test
                @DisplayName("예외를 던진다.")
                void it_throws_exception() throws Exception {

                    mockMvc.perform(delete("/tasks/1"))
                            .andExpect(status().isNotFound());
                }
            }

        }

        @Nested
        @DisplayName("path id가 0이하 라면")
        class Context_invalid_path_id {

            @Test
            @DisplayName("예외를 던진다.")
            void it_throws_exception() throws Exception {
                taskService.createTask(generateNewTask(TASK_TITLE_1));

                mockMvc.perform(delete("/tasks/0"))
                        .andExpect(status().isNotFound());
            }
        }
    }

}
