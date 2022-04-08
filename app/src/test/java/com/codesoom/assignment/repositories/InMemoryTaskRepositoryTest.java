package com.codesoom.assignment.repositories;

import com.codesoom.assignment.domains.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


class InMemoryTaskRepositoryTest {

    private TaskRepository repository;

    private static Long SAVED_TASK_ID;
    private static final Long NOT_EXIST_ID = 100L;

    @BeforeEach
    void setup() {
        this.repository = new InMemoryTaskRepository();

        Task task = new Task("title");
        final Task savedTask = this.repository.save(task);
        this.SAVED_TASK_ID = savedTask.getId();
    }

    @DisplayName("getTask()는 저장된 할 일을 모두 반환한다.")
    @Test
    void getTaskTest() {
        assertThat(repository.getTasks()).isNotEmpty();
    }

    @DisplayName("할 일을 성공적으로 저장한다.")
    @Test
    void saveTaskTest() {
        final int beforeSize = repository.getTasks().size();

        final Task savedTask = repository.save(new Task("hello"));

        final int afterSize = repository.getTasks().size();

        assertThat(afterSize - beforeSize).isEqualTo(1);
        assertThat(savedTask).isNotNull();
        assertThat(savedTask.getTitle()).isEqualTo("hello");
    }

    @DisplayName("id와 매칭되는 할 일이 있으면 해당 할 일을, 없으면 null을 반환한다.")
    @Test
    void findByIdTest() {
        assertThat(repository.findById(SAVED_TASK_ID)).isNotNull();
        assertThat(repository.findById(NOT_EXIST_ID)).isNull();
    }

    @DisplayName("할 일을 성공적으로 수정한다.")
    @Test
    void updateTaskTest() {
        final Task task = repository.findById(SAVED_TASK_ID);

        final Task updatedTask = repository.update(SAVED_TASK_ID, task.updateTitle("new title"));

        assertThat(repository.findById(SAVED_TASK_ID).getTitle()).isEqualTo("new title");
        assertThat(updatedTask.getTitle()).isEqualTo("new title");
    }

    @DisplayName("할 일을 성공적으로 삭제한다.")
    @Test
    void removeTest() {
        repository.remove(SAVED_TASK_ID);

        assertThat(repository.findById(SAVED_TASK_ID)).isNull();
    }


    @DisplayName("할 일이 새롭게 추가될 때 마다 id는 1씩 증가한다.")
    @Test
    void generateIdTest() {
        final Long id1 = repository.save(new Task("hello")).getId();
        final Long id2 = repository.save(new Task("hello")).getId();

        assertAll(() -> {
            assertThat(id1).isNotEqualTo(id2);
            assertThat(Long.compare(id1, id2)).isEqualTo(-1);
            assertThat(id2 - id1).isEqualTo(1L);
        });
    }

}
