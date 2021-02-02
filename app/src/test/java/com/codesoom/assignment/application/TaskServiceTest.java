package com.codesoom.assignment.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TaskService 클래스")
class TaskServiceTest {
    @Nested
    @DisplayName("getTasks 메서드는")
    class Describe_getTasks {

        @Nested
        @DisplayName("tasks 가 없을 때")
        class Context_without_tasks {

            @Test
            @DisplayName("빈 ArrayList 를 리턴한다.")
            void It_returns_empty_ArrayList() {

            }
        }

        @Nested
        @DisplayName("tasks 가 있을 때")
        class Context_with_tasks {

            @Test
            @DisplayName("tasks 가 들어있는 ArrayList 를 리턴한다.")
            void It_returns_empty_ArrayList() {

            }
        }
    }

    @Nested
    @DisplayName("getTask 메서드는")
    class Describe_getTask {
        @Nested
        @DisplayName("대상 id 가 없을 때")
        class Context_not_exists_target_id {

            @Test
            @DisplayName("TaskNotFoundException 을 던진다.")
            void It_throws_TaskNotFoundException() {

            }
        }

        @Nested
        @DisplayName("대상 id 가 있을 때")
        class Context_exists_target_id {

            @Test
            @DisplayName("task 를 리턴한다.")
            void It_returns_task() {

            }
        }
    }

    @Nested
    @DisplayName("createTask 메서드는")
    class Describe_createTask {

        @Test
        @DisplayName("생성된 task 를 리턴한다.")
        void It_returns_created_task() {

        }
    }

    @Nested
    @DisplayName("updateTask 메서드는")
    class Describe_updateTask {

        @Nested
        @DisplayName("대상 id 가 없을 때")
        class Context_not_exists_target_id {

            @Test
            @DisplayName("TaskNotFoundException 를 던진다.")
            void It_throws_TaskNotFoundException() {

            }
        }

        @Nested
        @DisplayName("대상 id 가 있을 때")
        class Context_exists_target_id {

            @Test
            @DisplayName("변경된 task 를 리턴한다.")
            void It_returns_modified_task() {

            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메서드는")
    class Describe_deleteTask {

        @Nested
        @DisplayName("대상 id 가 없을 때")
        class Context_not_exists_target_id {

            @Test
            @DisplayName("TaskNotFoundException 를 던진다.")
            void It_throws_TaskNotFoundException() {

            }
        }

        @Nested
        @DisplayName("대상 id 가 있을 때")
        class Context_exists_target_id {

            @Test
            @DisplayName("삭제된 task 를 리턴한다.")
            void It_returns_modified_task() {

            }
        }
    }
}