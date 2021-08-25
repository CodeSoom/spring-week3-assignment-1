package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.exceptions.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TaskController Web 테스트입니다.")
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {
    public static final String TEST_3 = "test3";
    public static final Long INVALID_ID = 10L;
    public static final String TASK_UPDATE_TITLE = "updateTitle";
    public static final long ONE = 1L;
    public static final long TWO = 2L;
    public static final long THREE = 3L;

    public static boolean deleted = false;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private Task test1;
    private Task test2;
    private Task test3;


    @BeforeEach
    void setUp() {
        test1 = new Task(ONE, "test1");
        test2 = new Task(TWO, "test2");
        test3 = Task.from("test3");

        given(taskService.getTasks())
                .willReturn(Arrays.asList(test1, test2));

        given(taskService.getTask(test1.getId()))
                .willReturn(test1)
                .willThrow(new TaskNotFoundException(test1.getId()));

        given(taskService.getTask(INVALID_ID))
                .willThrow(new TaskNotFoundException(INVALID_ID));

        given(taskService.createTask(test3))
                .willReturn(new Task(THREE, TEST_3));

        given(taskService.updateTask(test1.getId(), test3))
                .willReturn(new Task(test1.getId(), test3.getTitle()));

        given(taskService.updateTask(INVALID_ID, test3))
                .willThrow(new TaskNotFoundException(INVALID_ID));

        given(taskService.deleteTask(test1.getId()))
                .willReturn(null)
                .willThrow(new TaskNotFoundException(test1.getId()));

        given(taskService.deleteTask(test2.getId()))
                .willAnswer((invocation) -> {
                    deleted = true;
                    return null;
                });

        given(taskService.deleteTask(INVALID_ID))
                .willThrow(new TaskNotFoundException(INVALID_ID));

    }


    @DisplayName("할 일 전체 조회를 할 수 있습니다 - GET /tasks")
    @Test
    void list() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("test1")));
    }

    @DisplayName("할 일을 조회할 수 있습니다. - GET /tasks/{id}")
    @Test
    void detail() throws Exception {
        mockMvc.perform(get("/tasks/" + test1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(test1.getId()))
                .andExpect(jsonPath("$.title").value(test1.getTitle()))
                .andReturn();
    }

    @DisplayName("잘못된 식별자로 할 일을 조회하면 실패합니다 - GET /tasks/{invalidId}")
    @Test
    void detailInvalid() throws Exception {
        mockMvc.perform(get("/tasks/10"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("할 일을 생성할 수 있습니다. - POST /tasks")
    @Test
    void createTask() throws Exception {
        final String contents = objectMapper.writeValueAsString(test3);

        mockMvc.perform(TaskSteps.postWithConfig(contents))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("3"))
                .andExpect(jsonPath("$.title").value(TEST_3));
    }


    @DisplayName("할 일의 내용을 수정할 수 있습니다 - PUT OR PATCH /tasks/{id}")
    @Test
    void updateTask() throws Exception {
        final String contents = objectMapper.writeValueAsString(test3);

        mockMvc.perform(TaskSteps.putWithConfig(test1.getId(), contents))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value(TEST_3));

    }

    @DisplayName("존재하지 않거나 찾을 수 없는 식별자의 할 일을 수정하려 하면 실패합니다. - PUT OR PATCH /tasks/{invalidId}")
    @Test
    void updateTaskInvalidFromNotFoundId() throws Exception {
        final String contents = objectMapper.writeValueAsString(test3);

        mockMvc.perform(TaskSteps.putWithConfig(INVALID_ID, contents))
                .andExpect(status().isNotFound());
    }

    @DisplayName("유효하지 않은 타입의 식별자를 전달하면 실패합니다. - PUT OR PATCH /tasks/{invalidId}")
    @ParameterizedTest
    @MethodSource("provideInvalidArguments")
    void updateTaskInvalidType(Object arg) throws Exception {
        final String contents = objectMapper.writeValueAsString(test3);

        mockMvc.perform(TaskSteps.putWithConfig(arg, contents))
                .andExpect(status().isBadRequest());
    }

    public static Stream<Arguments> provideInvalidArguments() {
        return Stream.of(
                Arguments.of("abc"),
                Arguments.of(1.0)
        );
    }

    @DisplayName("할 일을 삭제할 수 있습니다 - DELETE /tasks/{id}")
    @Test
    void deleteTask() throws Exception {
        final String path = "/tasks/";

        mockMvc.perform(get(path + test1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(test1.getId()))
                .andExpect(jsonPath("$.title").value(test1.getTitle()))
                .andReturn();

        mockMvc.perform(delete(path + test1.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(path + test1.getId()))
                .andExpect(status().isNotFound());
    }

    @DisplayName("우효하지 않은 식별자의 할 일을 삭제하려 하면 실패합니다. - DELETE /tasks/{invalidId}")
    @Test
    void deleteTaskInvalid() throws Exception {
        mockMvc.perform(delete("/tasks/" + INVALID_ID))
                .andExpect(status().isNotFound());
    }
}
