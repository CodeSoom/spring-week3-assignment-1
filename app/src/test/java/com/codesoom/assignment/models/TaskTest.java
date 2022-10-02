package com.codesoom.assignment.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

// Task에 equals와 hashCode 메소드를 override 했더니
// 해당 두 메서드 JaCoCo 테스트에 계속 Missed Instructions가 떠서 추가해줌.
// 그래도 아직 Missed Branches 존재
class TaskTest {

    @Test
    @DisplayName("참조값이 달라도 인스턴스 값들이 같으면 같은 hashcode를 리턴한다.")
    void compareHashcode() {
        Task task1 = TaskFactory.withIdAndTitle(1L, "Test");
        Task task2 = TaskFactory.withIdAndTitle(1L, "Test");

        assertThat(task1.hashCode()).isEqualTo(task2.hashCode());
    }

    @Test
    void equalsTest() {
        Task task1 = TaskFactory.withIdAndTitle(1L, "Test");
        Task task2 = TaskFactory.withIdAndTitle(1L, "Test");
        Task task3 = TaskFactory.withIdAndTitle(1L, "Test2");
        Task task4 = task1;
        Task task5 = null;
        NewTask task6 = new NewTask(1L, "test");

        assertThat(task1.equals(task2)).isTrue();
        assertThat(task1.equals(task3)).isFalse();
        assertThat(task1.equals(task4)).isTrue();
        assertThat(task1.equals(task5)).isFalse();
        assertThat(task1.equals(task6)).isFalse();
    }

    static class NewTask {
        private Long id;
        private String title;

        public NewTask(Long id, String title) {
            this.id = id;
            this.title = title;
        }

    }
}