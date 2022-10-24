package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {
    private TaskService taskService;
    private final String TEST_TITLE = "테스트는 재밌군요!";
    private final String POSTFIX_TITLE = " 그치만 매우 생소하군요!";

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

        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo(TEST_TITLE);
    }

    @Test
    void getTasks() {
        List<Task> tasks = taskService.getTasks();

        // fixture 데이터가 잘 들어오는지 확인
        assertThat(tasks).hasSize(1);
    }

    @Test
    void getTaskWithValidId() {
        Task task = taskService.getTask(1L);

        // fixture 데이터의 id가 정상인지 확인
        assertThat(task.getId()).isEqualTo(1L);
        
        // fixture 데이터의 title이 정상인지 확인
        assertThat(task.getTitle()).isEqualTo(TEST_TITLE);
    }

    @Test
    void getTaskWithInValidId() {
        // fixture 데이터에 없는 id값으로 호출할 때 TaskNotFoundException 확인
        assertThatThrownBy(() -> taskService.getTask(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void createTask() {
        int oldSize = taskService.getTasks().size();

        Task source = taskService.getTask(1L);

        source.setTitle(POSTFIX_TITLE);

        Long oldId = source.getId();

        Task task = taskService.createTask(source);

        Long newId = task.getId();

        int newSize = taskService.getTasks().size();

        // id 값이 정상적으로 1씩 올라가는지 확인
        assertThat(newId - oldId).isEqualTo(1);
        
        // 할일 목록 List 사이즈가 증가했는지 확인
        assertThat(newSize - oldSize).isEqualTo(1);

        // title이 POSTFIX_TITLE로 잘 들어갔는지 확인
        assertThat(task.getTitle()).isEqualTo(POSTFIX_TITLE);
    }

    @Test
    void updateTask() {
        Task source = taskService.getTask(1L);
        source.setTitle(source.getTitle() + POSTFIX_TITLE);

        Task task = taskService.updateTask(1L, source);

        // id는 수정되지 않았는지 확인
        assertThat(task.getId()).isEqualTo(1L);

        // 수정된 title이 "테스트는 재밌군요! 그치만 매우 생소하군요!"로 수정 됐는지 확인
        assertThat(task.getTitle()).isEqualTo(TEST_TITLE + POSTFIX_TITLE);
    }

    @Test
    void deleteTask() {
        int oldSize = taskService.getTasks().size();

        taskService.deleteTask(1L);

        int newSize = taskService.getTasks().size();

        // 할일 목록 List 사이즈가 감소했는지 확인
        assertThat(oldSize - newSize).isEqualTo(1);

        // 지워진 id값으로 호출할 때 TaskNotFoundException 확인
        assertThatThrownBy(() -> taskService.getTask(1L))
                .isInstanceOf(TaskNotFoundException.class);
    }
}
