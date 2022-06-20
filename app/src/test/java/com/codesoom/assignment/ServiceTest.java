package com.codesoom.assignment;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ServiceTest {
    private TaskService service;

    @BeforeEach
    private void setUp() {
        this.service = new TaskService();
    }

    @Test
    public void createTask() {
        // GIVEN: List에 아무 것도 없는 상태

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
        List<Task> tasks = this.service.getTasks();

        assertThat(tasks).hasSize(0);
    }

    @Test
    public void getTasks_등록된_task_1개일_때() {
        Task createdTask = this.service.createTask(dummyTask("1"));
        this.service.createTask(createdTask);

        List<Task> tasks = this.service.getTasks();

        assertThat(tasks).hasSize(1);
        assertThat(tasks).contains(createdTask);
    }

    @Test
    public void getTasks_등록된_task_2개_이상일_때() {}

    @Test
    public void getTask_찾는_id가_없을_때() {}

    @Test
    public void getTask_찾는_id가_있을_때() {}

    @Test
    public void 새로운_task_생성() {}

    @Test
    public void 수정하려는_id가_없을_때() {}

    @Test
    public void 수정하려는_id가_있을_때() {}

    @Test
    public void 지우려는_id가_없을_때() {}

    @Test
    public void 지우려는_id가_있을_때() {}



    private Task dummyTask(String title) {
        Task newTask = new Task();
        newTask.setTitle(title);

        return newTask;
    }
}
