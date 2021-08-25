package com.codesoom.assignment.models;

import com.codesoom.assignment.models.data.FakeTask;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task 모델 테스트")
class TaskTest {

    public static final String title = "test";

    @DisplayName("할 일을 생성합니다.")
    @Test
    void create() {
        Task task = new Task(1L, title);

        assertThat(task.getTitle()).isEqualTo(title);
        assertThat(task.getId()).isEqualTo(1L);
    }

    @DisplayName("Task 객체는 동등성 비교를 합니다.")
    @Test
    void equivalenceTask() {
        final Task source = new Task(1L, title);
        final Task target = new Task(1L, title);
        final Task elseIdTask = new Task(2L, title);
        final Task elseTitleTask = new Task(1L, "elseTest");

        assertThat(source).isEqualTo(target);
        assertThat(source.equals(target)).isTrue();
        assertThat(source.equals(elseIdTask)).isFalse();
        assertThat(source.equals(elseTitleTask)).isFalse();

    }

    @DisplayName("Task 인스턴스 자기 자신과 비교할 경우 참을 반환합니다.")
    @Test
    void equivalenceTaskSelf() {
        final Task task = new Task(1L, title);
        assertThat(task.equals(task)).isTrue();
    }

    @DisplayName("내용이 같더라도 다른 객체와 비교할 경우 false를 반환합니다.")
    @Test
    void equivalenceInvalidType() {
        final Task task = new Task(1L, title);
        final FakeTask fakeTask = new FakeTask(1L, title);

        assertThat(task.getId()).isEqualTo(fakeTask.getId());
        assertThat(task.getTitle()).isEqualTo(fakeTask.getTitle());
        assertThat(task).isNotEqualTo(fakeTask);
    }


}
