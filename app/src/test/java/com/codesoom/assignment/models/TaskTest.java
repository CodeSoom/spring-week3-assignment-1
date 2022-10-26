package com.codesoom.assignment.models;

import com.codesoom.assignment.utils.NumberGenerator;
import com.codesoom.assignment.utils.RandomTitleGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaskTest 클래스의")
class TaskTest {

    @Nested
    @DisplayName("getId() 메소드는")
    class Describe_getId_method {

        private Long id;
        private Task task;

        @BeforeEach
        void setUp() {
            id = NumberGenerator.getRandomNotNegativeLong();
            task = new Task(id, null);
        }

        @Test
        @DisplayName("생성할 때 인자로 사용한 id와 똑같은 id를 리턴한다.")
        void it_returns_the_same_id_as_the_id_given_when_constructed() {
            assertThat(task.getId()).isEqualTo(id);
        }
    }

    @Nested
    @DisplayName("getTitle() 메소드는")
    class Describe_getTitle_method {

        private String title;
        private Task task;

        @BeforeEach
        void setUp() {
            title = RandomTitleGenerator.getRandomTitle();
            task = new Task(null, title);
        }

        @Test
        @DisplayName("생성할 때 인자로 사용한 title과 똑같은 title을 리턴한다.")
        void it_returns_the_same_title_as_the_title_given_when_constructed() {
            assertThat(task.getTitle()).isEqualTo(title);
        }
    }
}
