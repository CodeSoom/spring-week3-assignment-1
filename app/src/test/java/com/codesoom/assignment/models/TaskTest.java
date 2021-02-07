package com.codesoom.assignment.models;

import com.codesoom.assignment.App;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task 클래스")
public class TaskTest {
    private static final Long GIVEN_ID = 1L;
    private static final Long CHANGE_ID = 2L;
    private static final String GIVEN_TITLE = "homework";
    private static final String CHANGE_TITLE = "work";

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(GIVEN_ID);
        task.setTitle(GIVEN_TITLE);
    }

    @DisplayName("Task를 생성한다")
    @Test
    void create() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("STUDY");

        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo("STUDY");
    }

    @DisplayName("setId은 Task의 id를 변경한다")
    @Test
    void setId() {
        task.setId(CHANGE_ID);

        assertThat(task.getId()).isEqualTo(CHANGE_ID);
    }

    @DisplayName("setTitle은 Task의 title을 변경한다")
    @Test
    void setTitle() {
        task.setTitle(GIVEN_TITLE);

        assertThat(task.getTitle()).isEqualTo(GIVEN_TITLE);
    }

    @DisplayName("getId은 Task의 id을 리턴한다")
    @Test
    void getId() {
        assertThat(task.getId()).isEqualTo(GIVEN_ID);
    }

    @DisplayName("getTitle은 Task의 title을 리턴한다")
    @Test
    void getTitle() {
        assertThat(task.getTitle()).isEqualTo(GIVEN_TITLE);
    }

    @DisplayName("Equals와 해쉬코드 값을 테스트")
    @Test
    void equals_hashcode() {
        Task task1 = new Task();
        task1.setId(GIVEN_ID);
        task1.setTitle(GIVEN_TITLE);

        Task task2 = new Task();
        task2.setId(GIVEN_ID);
        task2.setTitle(GIVEN_TITLE);

        assertThat(task1.equals(task2)).isTrue();
        assertThat(task1.hashCode()).isEqualTo(task2.hashCode());
    }

    @Test
    void equals_with_different_object() {
        Task task = new Task();
        App app = new App();

        assertThat(task.equals(app)).isFalse();
    }

    @Test
    void equals_with_different_id() {
        Task task1 = new Task();
        task1.setId(GIVEN_ID);
        task1.setTitle(GIVEN_TITLE);

        Task task2 = new Task();
        task2.setId(CHANGE_ID);
        task2.setTitle(GIVEN_TITLE);

        assertThat(task1.equals(task2)).isFalse();
    }

    @Test
    void equals_with_different_title() {
        Task task1 = new Task();
        task1.setId(GIVEN_ID);
        task1.setTitle(GIVEN_TITLE);

        Task task2 = new Task();
        task2.setId(GIVEN_ID);
        task2.setTitle(CHANGE_TITLE);

        assertThat(task1.equals(task2)).isFalse();
    }

    @Test
    void equals_with_different_id_title() {
        Task task1 = new Task();
        task1.setId(GIVEN_ID);
        task1.setTitle(GIVEN_TITLE);

        Task task2 = new Task();
        task2.setId(CHANGE_ID);
        task2.setTitle(CHANGE_TITLE);

        assertThat(task1.equals(task2)).isFalse();
    }

}
