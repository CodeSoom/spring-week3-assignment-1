package com.codesoom.assignment.models;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskTest 클래스")
class TaskTest {
    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class Task_생성자는 {
        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 만약_매개변수로_주어진_title이_비어있다면 {
            String title = "";

            @Test
            @DisplayName("BadRequestException 예외를 발생시킨다")
            void BadRequestException_예외를_발생시킨다() {
                assertThatThrownBy(() -> new Task(title));
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class getId_메서드는 {
        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 만약_id가_1인_Task_객체가_주어진다면 {
            Task task() {
                Task task = new Task("과제하기");
                task.setId(1L);
                return task;
            }

            @Test
            @DisplayName("id의 값으로 1을 반환한다")
            void id의_값으로_1을_반환한다() {
                assertThat(task().getId()).isEqualTo(1L);
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class setId_메서드는 {
        Task task() {
            Task task = new Task("잠자기");
            return task;
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 만약_id로_2가_주어진다면 {
            Long id = 2L;

            @Test
            @DisplayName("Task의 id값을 2로 설정한다")
            void Task의_id값을_2로_설정한다() {
                Task task = task();
                task.setId(id);
                assertThat(task.getId()).isEqualTo(2L);
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 만약_id로_5가_주어진다면 {
            Long id = 5L;

            @Test
            @DisplayName("Task의 id값을 5로 설정한다")
            void Task의_id값을_5로_설정한다() {
                Task task = task();
                task.setId(id);
                assertThat(task.getId()).isEqualTo(5L);
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class getTitle_메서드는 {
        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 만약_title이_밥먹기인_Task_객체가_주어진다면 {
            Task task() {
                Task task = new Task("밥먹기");
                return task;
            }

            @Test
            @DisplayName("title의 값으로 밥먹기를 반환한다")
            void title의_값으로_밥먹기를_반환한다() {
                assertThat(task().getTitle()).isEqualTo("밥먹기");
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class updateTitle_메서드는 {
        Task task() {
            Task task = new Task("과제하기");
            return task;
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 만약_새로운_title로_노래하기가_주어진다면 {
            String newTitle = "노래하기";

            @Test
            @DisplayName("기존 Task의 title을 노래하기로 설정한다")
            void 기존_Task의_title을_노래하기로_설정한다() {
                Task task = task();
                task.updateTitle(newTitle);

                assertThat(task.getTitle()).isEqualTo("노래하기");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 만약_새로운_title로_밥먹기가_주어진다면 {
            String newTitle = "밥먹기";

            @Test
            @DisplayName("기존 Task의 title을 밥먹기로 설정한다")
            void 기존_Task의_title을_밥먹기로_설정한다() {
                Task task = task();
                task.updateTitle(newTitle);

                assertThat(task.getTitle()).isEqualTo("밥먹기");
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class validateTitle_메서드는 {
        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 만약_title이_비어있는_문자열이라면 {
            Task task() {
                Task task = new Task("");
                return task;
            }

            @Test
            @DisplayName("BadRequestException 예외를 발생시킨다")
            void BadRequestException_예외를_발생시킨다() {
                assertThatThrownBy(() -> task().validateTitle(task().getTitle()));
            }
        }
    }
}
