package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task 클래스의")
class TaskTest {

    private Task generateTask(long id, String title) {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        return task;
    }

    @Nested
    @DisplayName("equals 메소드는")
    class Describe_of_equals {

        @Nested
        @DisplayName("만약 Task Type과 관련없는 객체가 주어진다면")
        class Context_with_different_type_of_object {

            private Task task1;
            private Object notTask;

            @BeforeEach
            void setNonTaskObject() {
                this.task1 = generateTask(1L, "task1");
                this.notTask = new Object();
            }

            @Test
            @DisplayName("false를 리턴한다")
            void it_returns_false() {
                assertThat(task1.equals(notTask))
                        .isFalse();
            }
        }

        @Nested
        @DisplayName("만약 title이 다른 Task 객체가 주어진다면")
        class Context_with_different_title {

            private Task task1;
            private Task task2;

            @BeforeEach
            void setTasksTitleDifferent() {
                long sameId = 1L;
                this.task1 = generateTask(sameId, "task1");
                this.task2 = generateTask(sameId, "differentTitle");
            }

            @Test
            @DisplayName("false를 리턴한다")
            void it_returns_false() {
                assertThat(task1.equals(task2))
                        .isFalse();
            }
        }

        @Nested
        @DisplayName("만약 id가 다른 Task 객체가 주어진다면")
        class Context_with_different_id {

            private Task task1;
            private Task task2;

            @BeforeEach
            void setTaskIdDifferent() {
                long id = 1L;
                long differentId = id + 1;
                String sameTitle = "sameTitle";

                this.task1 = generateTask(id, sameTitle);
                this.task2 = generateTask(differentId, sameTitle);
            }

            @Test
            @DisplayName("false를 리턴한다")
            void it_returns_false() {
                assertThat(task1.equals(task2))
                        .isFalse();
            }
        }

        @Nested
        @DisplayName("만약 id와 title이 같은 객체가 주어진다면")
        class Context_with_attributes_are_same {

            private Task task1;
            private Task task2;

            @BeforeEach
            void setTasksIdAndTitleSame() {
                long sameId = 1L;
                String sameTitle = "sameTitle";

                this.task1 = generateTask(sameId, sameTitle);
                this.task2 = generateTask(sameId, sameTitle);
            }

            @Test
            @DisplayName("true를 리턴한다")
            void it_returns_true() {
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

            private Task task1;
            private Task task2;

            @BeforeEach
            void setTasksTitleDifferent() {
                long sameId = 1L;
                this.task1 = generateTask(sameId, "task1");
                this.task2 = generateTask(sameId, "differentTitle");
            }

            @Test
            @DisplayName("각각 다른 hashCode를 반환한다")
            void it_returns_different_hash_code() {
                Integer codeOfTask1 = task1.hashCode();
                Integer codeOfTask2 = task2.hashCode();

                assertThat(codeOfTask1.equals(codeOfTask2))
                        .isFalse();
            }
        }

        @Nested
        @DisplayName("만약 id가 다른 Task 두 객체로부터 호출된다면")
        class Context_with_differnt_id {

            private Task task1;
            private Task task2;

            @BeforeEach
            void setTaskIdDifferent() {
                long id = 1L;
                long differentId = id + 1;
                String sameTitle = "sameTitle";

                this.task1 = generateTask(id, sameTitle);
                this.task2 = generateTask(differentId, sameTitle);
            }

            @Test
            @DisplayName("각각 다른 hashCode를 반환한다")
            void if_returns_differnet_hash_code() {
                Integer codeOfTask1 = task1.hashCode();
                Integer codeOfTask2 = task2.hashCode();

                assertThat(codeOfTask1.equals(codeOfTask2))
                        .isFalse();
            }
        }

        @Nested
        @DisplayName("만약 attribute가 동일한 Task 객체들로부터 호출된다면")
        class Context_with_same_attributes {

            private Task task1;
            private Task task2;

            @BeforeEach
            void setTasksIdAndTitleSame() {
                long sameId = 1L;
                String sameTitle = "sameTitle";

                this.task1 = generateTask(sameId, sameTitle);
                this.task2 = generateTask(sameId, sameTitle);
            }

            @Test
            @DisplayName("각각 동일한 hashCode를 반환한다")
            void it_returns_same_hash_code() {
                Integer codeOfTask1 = task1.hashCode();
                Integer codeOfTask2 = task2.hashCode();

                assertThat(codeOfTask1.equals(codeOfTask2))
                        .isTrue();
            }
        }
    }
}
