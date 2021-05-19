package com.codesoom.assignment.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task 클래스의")
class TaskTest {

    @Nested
    @DisplayName("equals 메소드는")
    class Describe_of_equals {

        @Nested
        @DisplayName("만약 Task Type과 관련없는 객체가 주어진다면")
        class Context_with_different_type_of_object {

            final private Task task1;

            Context_with_different_type_of_object() {
                this.task1 = new Task();
                task1.setId(1L);
                task1.setTitle("task1");
            }

            @Test
            @DisplayName("false를 리턴한다")
            void it_returns_false() {
                Object notTask = new Object();

                assertThat(task1.equals(notTask))
                        .isFalse();
            }
        }

        @Nested
        @DisplayName("만약 title이 다른 Task 객체가 주어진다면")
        class Context_with_differnt_title {

            final private Task task1;

            Context_with_differnt_title() {
                this.task1 = new Task();
                task1.setId(1L);
                task1.setTitle("task1");
            }

            @Test
            @DisplayName("false를 리턴한다")
            void it_retuns_false() {
                Task task2 = new Task();
                task2.setId(1L);
                task2.setTitle("differentTitle");

                assertThat(task1.equals(task2))
                        .isFalse();
            }
        }

        @Nested
        @DisplayName("만약 id가 다른 Task 객체가 주어진다면")
        class Context_with_differnt_id {

            final private Task task1;

            Context_with_differnt_id() {
                this.task1 = new Task();
                task1.setId(1L);
                task1.setTitle("task1");
            }

            @Test
            @DisplayName("false를 리턴한다")
            void it_returns_false() {
                Task task2 = new Task();
                long differentId = task1.getId() + 1L;
                task2.setId(differentId);
                task2.setTitle("task1");

                assertThat(task1.equals(task2))
                        .isFalse();
            }
        }

        @Nested
        @DisplayName("만약 id와 title이 같은 객체가 주어진다면")
        class Context_with_attributes_are_same {

            final private Task task1;

            Context_with_attributes_are_same() {
                this.task1 = new Task();
                task1.setId(1L);
                task1.setTitle("task1");
            }

            @Test
            @DisplayName("true를 리턴한다")
            void it_returns_true() {
                Task task2 = new Task();
                task2.setId(task1.getId());
                task2.setTitle(task1.getTitle());

                assertThat(task1.equals(task2))
                        .isTrue();
            }
        }

    }

    @Nested
    @DisplayName("hashCode 메소드는")
    class Describe_of_hashCode {

        @Nested
        @DisplayName("만약 title이 다른 Task 두 객체로부터 호출된다면")
        class Context_with_differnt_title {

            @Test
            @DisplayName("각각 다른 hashCode를 반환한다")
            void it_returns_different_hash_code() {
                Task task1 = new Task();
                task1.setId(1L);
                task1.setTitle("task1");

                Task task2 = new Task();
                task2.setId(1L);
                task2.setTitle("task2");

                Integer codeOfTask1 = task1.hashCode();
                Integer codeOfTask2 = task2.hashCode();

                assertThat(codeOfTask1.equals(codeOfTask2))
                        .isFalse();
            }
        }

        @Nested
        @DisplayName("만약 id가 다른 Task 두 객체로부터 호출된다면")
        class Context_with_differnt_id {

            @Test
            @DisplayName("각각 다른 hashCode를 반환한다")
            void if_returns_differnet_hash_code() {
                Task task1 = new Task();
                task1.setId(1L);
                task1.setTitle("task1");

                Task task2 = new Task();
                task2.setId(2L);
                task2.setTitle("task1");

                Integer codeOfTask1 = task1.hashCode();
                Integer codeOfTask2 = task2.hashCode();

                assertThat(codeOfTask1.equals(codeOfTask2))
                        .isFalse();
            }
        }

        @Nested
        @DisplayName("만약 attribute가 동일한 Task 객체들로부터 호출된다면")
        class Context_with_same_attributes {

            @Test
            @DisplayName("각각 동일한 hashCode를 반환한다")
            void it_returns_same_hash_code() {
                Task task1 = new Task();
                task1.setId(1L);
                task1.setTitle("task1");

                Task task2 = new Task();
                task2.setId(1L);
                task2.setTitle("task1");

                Integer codeOfTask1 = task1.hashCode();
                Integer codeOfTask2 = task2.hashCode();

                assertThat(codeOfTask1.equals(codeOfTask2))
                        .isTrue();
            }
        }
    }
}