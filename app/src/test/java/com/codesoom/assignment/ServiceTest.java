package com.codesoom.assignment;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ServiceTest {
    private TaskService service;

    @BeforeEach
    private void setUp() {
        this.service = new TaskService();
    }

    @Test
    public void createTask() {
        // GIVEN: 등록된 Task가 아무 것도 없는 상태

        // WHEN: 3개의 dummy task가 추가됨
        Task createdTask1 = this.service.createTask(dummyTask("1"));
        Task createdTask2 = this.service.createTask(dummyTask("2"));
        Task createdTask3 = this.service.createTask(dummyTask("3"));

        // THEN
            // 1. List에 3개의 dummy task가 잘 등록됨
        List<Task> tasks = this.service.getTasks();
        assertThat(tasks).hasSize(3);
        assertThat(tasks).contains(createdTask1);
        assertThat(tasks).contains(createdTask2);
        assertThat(tasks).contains(createdTask3);
        assertThat(tasks).doesNotContain(dummyTask("dummy"));

            // 2. 3개의 dummy task는 모두 다른 ID를 가짐
        assertThat(createdTask1.getId()).isNotEqualTo(createdTask2.getId());
        assertThat(createdTask2.getId()).isNotEqualTo(createdTask3.getId());
        assertThat(createdTask3.getId()).isNotEqualTo(createdTask1.getId());
    }

    @Test
    public void getTasks_등록된_task_없을_때() {
        // GIVEN: 등록된 Task가 없는 상태

        // WHEN: getTasks() 함수 호출
        List<Task> tasks = this.service.getTasks();

        // THEN: 빈 List가 반환
        assertThat(tasks).hasSize(0);
    }

    @Test
    public void getTasks_등록된_task_1개일_때() {
        // GIVEN: 하나의 Task만 등록된 상태
        Task createdTask = this.service.createTask(dummyTask("1"));
        this.service.createTask(createdTask);

        // WHEN: getTasks() 함수 호출
        List<Task> tasks = this.service.getTasks();

        // THEN: 하나의 element를 가진 List가 반환
        assertThat(tasks).hasSize(1);
        assertThat(tasks).contains(createdTask);
    }

    @Test
    public void getTasks_등록된_task_2개_이상일_때() {
        // GIVEN: 2개 이상의 task가 등록된 상태
        Task createdTask1 = this.service.createTask(dummyTask("1"));
        Task createdTask2 = this.service.createTask(dummyTask("2"));
        Task createdTask3 = this.service.createTask(dummyTask("3"));
        this.service.createTask(createdTask1);
        this.service.createTask(createdTask2);
        this.service.createTask(createdTask3);

        // WHEN: getTasks() 함수 호출
        List<Task> tasks = this.service.getTasks();

        // THEN: 등록된 모든 task가 List 형태로 반환
        assertThat(tasks).hasSize(3);
        assertThat(tasks).contains(createdTask1);
        assertThat(tasks).contains(createdTask2);
        assertThat(tasks).contains(createdTask3);
        assertThat(tasks).doesNotContain(dummyTask("dummy"));
    }

    @Test
    public void getTask_찾는_id가_없을_때() {
        // GIVEN: 여러 task가 등록된 상태
        Task createdTask1 = this.service.createTask(dummyTask("1"));
        Task createdTask2 = this.service.createTask(dummyTask("2"));
        Task createdTask3 = this.service.createTask(dummyTask("3"));
        this.service.createTask(createdTask1);
        this.service.createTask(createdTask2);
        this.service.createTask(createdTask3);

        // WHEN: 등록되지 않은 ID를 조회하려고 함
        assertThatThrownBy(() -> this.service.getTask(100L))
        // THEN: Throws TaskNotFoundException
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    public void getTask_찾는_id가_있을_때() {
        // GIVEN: 여러 task가 등록된 상태
        Task createdTask1 = this.service.createTask(dummyTask("1"));
        Task createdTask2 = this.service.createTask(dummyTask("2"));
        Task createdTask3 = this.service.createTask(dummyTask("3"));
        this.service.createTask(createdTask1);
        this.service.createTask(createdTask2);
        this.service.createTask(createdTask3);

        // WHEN: 등록되어있는 ID를 조회하려고 함
        Task task1 = this.service.getTask(createdTask1.getId());
        Task task2 = this.service.getTask(createdTask2.getId());
        Task task3 = this.service.getTask(createdTask3.getId());

        // THEN: Throws TaskNotFoundException
        assertThat(task1).isNotNull();
        assertThat(task1).isEqualTo(createdTask1);
        assertThat(task2).isNotNull();
        assertThat(task2).isEqualTo(createdTask2);
        assertThat(task3).isNotNull();
        assertThat(task3).isEqualTo(createdTask3);
    }

    @Test
    public void 새로운_task_생성() {
        // GIVEN:

        // WHEN:

        // THEN:

    }

    @Test
    public void 수정하려는_id가_없을_때() {
        // GIVEN:

        // WHEN:

        // THEN:

    }

    @Test
    public void 수정하려는_id가_있을_때() {
        // GIVEN:

        // WHEN:

        // THEN:

    }

    @Test
    public void 지우려는_id가_없을_때() {
        // GIVEN:

        // WHEN:

        // THEN:

    }

    @Test
    public void 지우려는_id가_있을_때() {
        // GIVEN:

        // WHEN:

        // THEN:

    }



    private Task dummyTask(String title) {
        Task newTask = new Task();
        newTask.setTitle(title);

        return newTask;
    }
}
