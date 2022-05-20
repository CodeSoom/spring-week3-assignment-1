package com.codesoom.assignment.models;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TaskTest_클래스의 {
    private static final Long TASK_ID_ONE = 1L;
    private static final Long TASK_ID_TWO = 2L;
    private static final String TASK_TITLE_ONE = "test_One";
    private static final String TASK_TITLE_TWO = "test_Two";

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(TASK_ID_ONE);
        task.setTitle(TASK_TITLE_ONE);
    }

    @Nested
    class getId_메서드는 {
        @Nested
        class 만약_Task에_값이_있다면 {
            @Test
            void id를_가져올_수_있다() {
                assertThat(task.getId()).isEqualTo(TASK_ID_ONE);
            }
        }
    }

    @Nested
    class setId_메서드는 {
        @Nested
        class 만약_Task에_값이_없다면 {
            @Test
            void id를_저장할_수_있다() {
                task.setId(TASK_ID_TWO);
                assertThat(task.getId()).isEqualTo(TASK_ID_TWO);
            }
        }
    }

    @Nested
    class getTitle_메서드는 {
        @Nested
        class 만약_Task에_값이_있다면 {
            @Test
            void Title을_가져올_수_있다() {
                assertThat(task.getTitle()).isEqualTo(TASK_TITLE_ONE);
            }
        }
    }

    @Nested
    class setTitle_메서드는 {
        @Nested
        class 만약_Task에_값이_없다면 {
            @Test
            void Title을_저장할_수_있다() {
                task.setTitle(TASK_TITLE_TWO);
                assertThat(task.getTitle()).isEqualTo(TASK_TITLE_TWO);
            }
        }
    }
}
