package com.codesoom.assignment.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

    @Test
    void testEqualsWithDifferentTypeOfObject() {
        // given
        Task task1 = new Task();
        task1.setTitle("task1");

        Object notTask = new Object();

        // when
        assertThat(task1.equals(notTask))
        // then
                .isFalse();
    }

    @Test
    void testEqualsWithDifferentTitleOfTask() {
        // given
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("task1");

        Task task2 = new Task();
        task2.setId(1L);
        task2.setTitle("differentTitle");

        // when
        assertThat(task1.equals(task2))
        // then
                .isFalse();
    }

    @Test
    void testEqualsWithDifferentIdOfTask() {
        // given
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("task1");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("task1");

        // when
        assertThat(task1.equals(task2))
        // then
                .isFalse();
    }

    @Test
    void testEqualsWithSameTask() {
        // given
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("task1");

        Task task2 = new Task();
        task2.setId(1L);
        task2.setTitle("task1");

        // when
        assertThat(task1.equals(task2))
        // then
                .isTrue();
    }

    @Test
    void testHashCodeWithDifferentTitleOfTask() {
        // given
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("task1");

        Task task2 = new Task();
        task2.setId(1L);
        task2.setTitle("task2");

        Integer codeOfTask1 = task1.hashCode();
        Integer codeOfTask2 = task2.hashCode();

        // when
        assertThat(codeOfTask1.equals(codeOfTask2))
        // then
                .isFalse();
    }

    @Test
    void testHashCodeWithDifferentIdOfTask() {
        // given
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("task1");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("task1");

        Integer codeOfTask1 = task1.hashCode();
        Integer codeOfTask2 = task2.hashCode();

        // when
        assertThat(codeOfTask1.equals(codeOfTask2))
                // then
                .isFalse();
    }

    @Test
    void testHashCodeWithSameTask() {
        // given
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("task1");

        Task task2 = new Task();
        task2.setId(1L);
        task2.setTitle("task1");

        Integer codeOfTask1 = task1.hashCode();
        Integer codeOfTask2 = task2.hashCode();

        // when
        assertThat(codeOfTask1.equals(codeOfTask2))
                // then
                .isTrue();
    }

    @Test
    void testHashCodeWithNullId() {
        // given
        Task task1 = new Task();
        task1.setTitle("task1");

        Task task2 = new Task();
        task2.setTitle("task1");

        Integer codeOfTask1 = task1.hashCode();
        Integer codeOfTask2 = task2.hashCode();

        // when
        assertThat(codeOfTask1.equals(codeOfTask2))
                // then
                .isTrue();
    }

    @Test
    void testHashCodeWithNullTitle() {
        // given
        Task task1 = new Task();
        task1.setId(1L);

        Task task2 = new Task();
        task2.setId(1L);

        Integer codeOfTask1 = task1.hashCode();
        Integer codeOfTask2 = task2.hashCode();

        // when
        assertThat(codeOfTask1.equals(codeOfTask2))
                // then
                .isTrue();
    }

    @Test
    void testHashCodeWithNullIdAndTitle() {
        // given
        Task task1 = new Task();

        Task task2 = new Task();

        Integer codeOfTask1 = task1.hashCode();
        Integer codeOfTask2 = task2.hashCode();

        // when
        assertThat(codeOfTask1.equals(codeOfTask2))
        // then
                .isTrue();

        // given
        task1.setTitle("task1");
        Integer codeOfTitledTask = task1.hashCode();

        // when
        assertThat(codeOfTitledTask.equals(codeOfTask2))
        // then
                .isFalse();
    }
}