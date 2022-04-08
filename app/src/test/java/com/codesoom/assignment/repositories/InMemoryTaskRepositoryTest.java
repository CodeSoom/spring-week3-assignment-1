package com.codesoom.assignment.repositories;

import com.codesoom.assignment.domains.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("InMemoryTaskRepository 클래스")
class InMemoryTaskRepositoryTest {

    private TaskRepository repository;

    private static final Long NOT_EXIST_ID = 100L;

    @BeforeEach
    void setup() {
        this.repository = new InMemoryTaskRepository();
    }

    @DisplayName("getTasks 메서드는")
    @Nested
    class Describe_getTask {
        @DisplayName("저장된 할 일을 모두 반환한다.")
        @Test
        void getTaskTest() {
            assertThat(repository.getTasks()).isNotEmpty();
        }
    }

    @DisplayName("save 메서드는")
    @Nested
    class Describe_save {

        private static final String TITLE = "hello";

        @DisplayName("할 일을 성공적으로 저장한다.")
        @Test
        void saveTaskTest() {
            final int beforeSize = repository.getTasks().size();

            final Task savedTask = repository.save(new Task(TITLE));

            final int afterSize = repository.getTasks().size();

            assertThat(afterSize - beforeSize).isEqualTo(1);
            assertThat(savedTask).isNotNull();
            assertThat(savedTask.getTitle()).isEqualTo(TITLE);
        }
    }

    @DisplayName("findById 메서드는")
    @Nested
    class Describe_findById {

        private Long SAVED_TASK_ID = 0L;

        @BeforeEach
        void setup() {
            repository = new InMemoryTaskRepository();

            Task task = new Task("title");
            final Task savedTask = repository.save(task);
            this.SAVED_TASK_ID = savedTask.getId();
        }

        @DisplayName("id와 매칭되는 할 일이 있으면")
        @Nested
        class Context_with_exist_id {
            @DisplayName("찾은 할 일을 반환한다.")
            @Test
            void it_return_found_Task() {
                assertThat(repository.findById(SAVED_TASK_ID)).isNotNull();
            }
        }

        @DisplayName("id와 매칭되는 할 일이 없으면")
        @Nested
        class Context_with_not_exist_id {
            @DisplayName("null을 반환한다.")
            @Test
            void it_return_null() {
                assertThat(repository.findById(NOT_EXIST_ID)).isNull();
            }
        }
    }

    @DisplayName("update 메서드는")
    @Nested
    class Describe_update {

        private Long SAVED_TASK_ID = 0L;
        private static final String UPDATE_TITLE = "new title";

        @BeforeEach
        void setup() {
            repository = new InMemoryTaskRepository();

            Task task = new Task("title");
            final Task savedTask = repository.save(task);
            this.SAVED_TASK_ID = savedTask.getId();
        }

        @DisplayName("id와 매칭되는 할 일이 있으면")
        @Nested
        class Context_with_exist_id {

            @DisplayName("수정된 할 일을 반환한다.")
            @Test
            void it_return_updated_Task() {
                final Task task = repository.findById(SAVED_TASK_ID);

                final Task updatedTask = repository.update(SAVED_TASK_ID, task.updateTitle(UPDATE_TITLE));

                assertThat(repository.findById(SAVED_TASK_ID).getTitle()).isEqualTo(UPDATE_TITLE);
                assertThat(updatedTask.getTitle()).isEqualTo(UPDATE_TITLE);
            }
        }

        @DisplayName("id와 매칭되는 할 일이 없으면")
        @Nested
        class Context_with_not_exist_id {
            @DisplayName("null을 반환한다.")
            @Test
            void it_return_null() {
                final Task updatedTask = repository.update(NOT_EXIST_ID, new Task(UPDATE_TITLE));

                assertThat(updatedTask).isNull();
            }
        }
    }

    @DisplayName("remove 메서드는")
    @Nested
    class Describe_remove {

        private Long SAVED_TASK_ID;

        @BeforeEach
        void setup() {
            repository = new InMemoryTaskRepository();

            Task task = new Task("title");
            final Task savedTask = repository.save(task);
            this.SAVED_TASK_ID = savedTask.getId();
        }

        @DisplayName("id와 일치하는 할 일을 삭제한다.")
        @Test
        void it_will_remove_by_id() {
            repository.remove(SAVED_TASK_ID);

            assertThat(repository.findById(SAVED_TASK_ID)).isNull();
        }
    }

    @DisplayName("id는")
    @Nested
    class Describe_generateId {
        @DisplayName("할 일이 새롭게 추가될 때 마다 1씩 증가한다.")
        @Test
        void it_will_return_added_id() {
            final Long id1 = repository.save(new Task("hello")).getId();
            final Long id2 = repository.save(new Task("hello")).getId();

            assertAll(() -> {
                assertThat(id1).isNotEqualTo(id2);
                assertThat(Long.compare(id1, id2)).isEqualTo(-1);
                assertThat(id2 - id1).isEqualTo(1L);
            });
        }
    }

}
