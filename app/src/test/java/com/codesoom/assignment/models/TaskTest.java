package com.codesoom.assignment.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

    @Nested
    @DisplayName("equals 메소드는")
    class Describe_Equals{

        private final Task compareTask = new Task();

        @Nested
        @DisplayName("비교할 객체가 null이거나 같은 클래스가 아니라면")
        class Context_NullObjectAndDiffClass{
            private final Task nullTask = null;
            private final String diffObject = "String";

            @Test
            @DisplayName("false를 반환한다")
            void It_NullOrDiffClass(){
                assertThat(compareTask.equals(nullTask)).isFalse();
                assertThat(compareTask.equals(diffObject)).isFalse();
            }
        }

        @Nested
        @DisplayName("비교할 객체와 주소가 같다면")
        class Context_SameAddress{
            @Test
            @DisplayName("true를 반환한다")
            void It_AddressEqualsTrue(){
                Task afterTask = compareTask;
                assertThat(afterTask.equals(compareTask)).isTrue();
            }
        }

        @Nested
        @DisplayName("비교할 객체의 식별자나 제목이 다르다면")
        class Context_DiffField{
            private final Task compareTask = new Task(1L , "COMPARE");
            private final Task sameTask = new Task(1L , "COMPARE");
            private final Task idDiff = new Task(2L , "COMPARE");
            private final Task titleDiff = new Task(1L , "DIFF COMPARE");

            @Test
            @DisplayName("false를 반환한다")
            void It_DiffField(){
                assertThat(compareTask.equals(sameTask)).isTrue();
                assertThat(compareTask.equals(idDiff)).isFalse();
                assertThat(compareTask.equals(titleDiff)).isFalse();
            }
        }
    }

    @Nested
    @DisplayName("hashCode 메소드는")
    class Describe_HashCode{
        @Nested
        @DisplayName("호출한 각 객체의 필드 기준 hashCode를 반환하며")
        class Context_HashCode{
            private final Task compareTask = new Task(1L , "COMPARE");
            private final Task sameTask = new Task(1L , "COMPARE");
            private final Task idDiff = new Task(2L , "COMPARE");
            private final Task titleDiff = new Task(1L , "DIFF COMPARE");

            @Test
            @DisplayName("식별자 또는 제목이 다르면 서로 다른 값을 반환한다")
            void It_ReturnHashCode(){
                assertThat(compareTask.hashCode()).isEqualTo(sameTask.hashCode());
                assertThat(compareTask.hashCode()).isNotEqualTo(idDiff.hashCode());
                assertThat(compareTask.hashCode()).isNotEqualTo(titleDiff.hashCode());
            }
        }
    }
}
