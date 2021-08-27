package com.codesoom.assignment.models;

import com.codesoom.assignment.models.data.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task 모델 테스트")
class TaskTest {

    public static final String TITLE = "test";

    @DisplayName("할 일을 생성합니다.")
    @Test
    void create() {
        Task task = new Task(1L, TITLE);

        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo(TITLE);
    }

    @DisplayName("Task 객체는 동등성 비교를 합니다.")
    @Test
    void equivalenceTask() {
        final Long id = 1L;
        final String title = "test";

        assertThat(new Task(id, title)).isEqualTo(new Task(id, title));
        assertThat(new Task(id, title)).isNotEqualTo(new Task(1234L, title));
        assertThat(new Task(id, title)).isNotEqualTo(new Task(id, "Other"));
        assertThat(new Task(id, title)).isNotEqualTo(new Task(1234L, "Other"));

    }

    @DisplayName("Task 인스턴스 자기 자신과 비교할 경우 참을 반환합니다.")
    @Test
    void equivalenceTaskSelf() {
        final Task task = new Task(1L, TITLE);
        assertThat(task.equals(task)).isTrue();
        assertThat(task).hasSameHashCodeAs(task);
    }

    @DisplayName("내용이 같더라도 다른 객체와 비교할 경우 false를 반환합니다.")
    @Test
    void equivalenceInvalidType() {
        final Task task = new Task(1L, TITLE);
        final Book book = new Book(1L, TITLE);

        assertThat(task.getId()).isEqualTo(book.getId());
        assertThat(task.getTitle()).isEqualTo(book.getTitle());
        assertThat(task).isNotEqualTo(book);
    }


}
