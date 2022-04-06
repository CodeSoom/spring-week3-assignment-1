package com.codesoom.assignment.repositories;

import com.codesoom.assignment.domains.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class InMemoryTaskRepositoryImplTest {

    private TaskRepository repository;

    private static final Long TASK_ID = 1L;
    private static final Long NOT_EXIST_ID = 100L;
    private static final Task TASK = new Task(TASK_ID, "title");

    @BeforeEach
    void setup() {
        repository = new InMemoryTaskRepositoryImpl();
    }

    @AfterEach
    void cleanup() {
        repository.remove(TASK_ID);
    }

    @DisplayName("저장된 할 일이 없으면 빈 값을 반환한다.")
    @Test
    void getTasksTest() {
        assertThat(repository.getTasks()).isEmpty();
    }

    @DisplayName("할 일을 성공적으로 저장한다.")
    @Test
    void saveTaskTest() {
        repository.save(TASK);

        assertThat(repository.getTasks()).isNotEmpty();
    }

    @DisplayName("id와 매칭되는 할 일이 있으면 해당 할 일을, 없으면 null을 반환한다.")
    @Test
    void findByIdTest() {
        repository.save(TASK);

        assertThat(repository.findById(TASK_ID)).isNotNull();
        assertThat(repository.findById(NOT_EXIST_ID)).isNull();
    }

    @DisplayName("할 일을 성공적으로 수정한다.")
    @Test
    void updateTaskTest() {
        repository.save(TASK);
        final Task task = repository.findById(TASK_ID);

        repository.update(TASK_ID, task.updateTitle("new title"));

        assertThat(repository.findById(TASK_ID).getTitle()).isEqualTo("new title");
    }

    @DisplayName("할 일을 성공적으로 삭제한다.")
    @Test
    void removeTest() {
        repository.save(TASK);

        repository.remove(TASK_ID);

        assertThat(repository.findById(TASK_ID)).isNull();
    }


    @DisplayName("generatedId()는 1씩 증가하는 id를 생성해 반환한다.")
    @Test
    void generateIdTest() {
        final Long id1 = repository.generateId();
        final Long id2 = repository.generateId();

        assertAll(() -> {
            assertThat(id1).isNotEqualTo(id2);
            assertThat(Long.compare(id1, id2)).isEqualTo(-1);
            assertThat(id2 - id1).isEqualTo(1L);
        });
    }
}