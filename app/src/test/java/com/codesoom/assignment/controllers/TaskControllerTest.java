package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.dto.TaskRequest;
import com.codesoom.assignment.exceptions.BadRequestException;
import com.codesoom.assignment.exceptions.NotFoundException;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskControllerTest 클래스")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TaskControllerTest {
    @Nested
    class getTasks_메서드는 {
        @Nested
        class 만약_Task가_추가되지_않은_상태라면 {
            TaskController taskController() {
                return new TaskController(new TaskService());
            }

            @Test
            void 크기가_0인_TaskResponse_리스트를_반환한다() {
                assertThat(taskController().getTasks()).hasSize(0);
            }
        }

        @Nested
        class 만약_Task가_1개_추가된_상태라면 {
            TaskController taskController() {
                TaskController taskController = new TaskController(new TaskService());
                taskController.create(new TaskRequest("과제하기"));
                return taskController;
            }

            @Test
            void 크기가_1인_TaskResponse_리스트를_반환한다() {
                assertThat(taskController().getTasks()).hasSize(1);
            }
        }
    }

    @Nested
    class getTask_메서드는 {
        TaskController taskController() {
            TaskController taskController = new TaskController(new TaskService());
            taskController.create(new TaskRequest("과제하기"));
            return taskController;
        }

        @Nested
        class 만약_값이_1인_id가_주어진다면 {
            TaskController taskController = taskController();
            Long id = 1L;

            @Test
            void id가_1인_TaskResponse_객체를_반환한다() {
                assertThat(taskController.getTask(id).getId()).isEqualTo(1L);
            }
        }

        @Nested
        class 만약_존재하지_않는_id가_주어진다면 {
            TaskController taskController = taskController();
            Long id = 100L;

            @Test
            void NotFoundException_예외를_발생시킨다() {
                assertThatThrownBy(() -> taskController.getTask(id))
                        .isInstanceOf(NotFoundException.class)
                        .hasMessageContaining("Task not found: " + id);
            }
        }
    }

    @Nested
    class create_메서드는 {
        TaskController taskController() {
            return new TaskController(new TaskService());
        }

        @Nested
        class 만약_title이_빈문자열인_TaskRequest가_주어진다면 {
            TaskController taskController = taskController();
            TaskRequest taskRequest() {
                return new TaskRequest("");
            }

            @Test
            void BadRequestException_예외를_발생시킨다() {
                assertThatThrownBy(() -> taskController.create(taskRequest()))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessageContaining("Bad Request");
            }
        }

        @Nested
        class 만약_title이_과제하기인_TaskRequest가_주어진다면 {
            TaskController taskController = taskController();
            TaskRequest taskRequest() {
                return new TaskRequest("과제하기");
            }

            @Test
            void title이_과제하기인_TaskResponse를_반환한다() {
                assertThat(taskController.create(taskRequest()).getTitle()).isEqualTo("과제하기");
            }
        }
    }

    @Nested
    class update_메서드는 {
        TaskController taskController() {
            TaskController taskController = new TaskController(new TaskService());
            taskController.create(new TaskRequest("과제하기"));
            return taskController;
        }

        @Nested
        class 만약_존재하지_않는_id가_주어진다면 {
            TaskController taskController = taskController();
            Long id = 100L;
            TaskRequest taskRequest() {
                return new TaskRequest("밥먹기");
            }

            @Test
            void NotFoundException_예외를_발생시킨다() {
                assertThatThrownBy(() -> taskController.update(id, taskRequest()))
                        .isInstanceOf(NotFoundException.class)
                        .hasMessageContaining("Task not found: " + id);
            }
        }

        @Nested
        class 만약_title이_빈문자열인_TaskRequest가_주어진다면 {
            TaskController taskController = taskController();
            Long id = 1L;
            TaskRequest taskRequest() {
                return new TaskRequest("");
            }

            @Test
            void BadRequestException_예외를_발생시킨다() {
                assertThatThrownBy(() -> taskController.update(id, taskRequest()))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessageContaining("Bad Request");
            }
        }

        @Nested
        class 만약_title이_밥먹기인_TaskRequest가_주어진다면 {
            TaskController taskController = taskController();
            Long id = 1L;
            TaskRequest taskRequest() {
                return new TaskRequest("밥먹기");
            }

            @Test
            void 해당_Task의_title을_밥먹기로_수정_후_TaskResponse를_반환한다() {
                assertThat(taskController.getTask(id).getTitle()).as("update 전 Task의 title은 과제하기이다").isEqualTo("과제하기");
                assertThat(taskController.update(id, taskRequest()).getTitle()).as("반환되는 TaskResponse의 title은 밥먹기이다").isEqualTo("밥먹기");
                assertThat(taskController.getTask(id).getTitle()).as("update 후 Task의 title은 밥먹기이다").isEqualTo("밥먹기");
            }
        }
    }

    @Nested
    class patch_메서드는 {
        TaskController taskController() {
            TaskController taskController = new TaskController(new TaskService());
            taskController.create(new TaskRequest("과제하기"));
            return taskController;
        }

        @Nested
        class 만약_존재하지_않는_id가_주어진다면 {
            TaskController taskController = taskController();
            Long id = 100L;
            TaskRequest taskRequest() {
                return new TaskRequest("밥먹기");
            }

            @Test
            void NotFoundException_예외를_발생시킨다() {
                assertThatThrownBy(() -> taskController.patch(id, taskRequest()))
                        .isInstanceOf(NotFoundException.class)
                        .hasMessageContaining("Task not found: " + id);
            }
        }

        @Nested
        class 만약_title이_빈문자열인_TaskRequest가_주어진다면 {
            TaskController taskController = taskController();
            Long id = 1L;
            TaskRequest taskRequest() {
                return new TaskRequest("");
            }

            @Test
            void BadRequestException_예외를_발생시킨다() {
                assertThatThrownBy(() -> taskController.patch(id, taskRequest()))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessageContaining("Bad Request");
            }
        }

        @Nested
        class 만약_title이_밥먹기인_TaskRequest가_주어진다면 {
            TaskController taskController = taskController();
            Long id = 1L;
            TaskRequest taskRequest() {
                return new TaskRequest("밥먹기");
            }

            @Test
            void 해당_Task의_title을_밥먹기로_수정_후_TaskResponse를_반환한다() {
                assertThat(taskController.getTask(id).getTitle()).as("patch 전 Task의 title은 과제하기이다").isEqualTo("과제하기");
                assertThat(taskController.patch(id, taskRequest()).getTitle()).as("반환되는 TaskResponse의 title은 밥먹기이다").isEqualTo("밥먹기");
                assertThat(taskController.getTask(id).getTitle()).as("patch 후 Task의 title은 밥먹기이다").isEqualTo("밥먹기");
            }
        }
    }

    @Nested
    class delete_메서드는 {
        TaskController taskController() {
            TaskController taskController = new TaskController(new TaskService());
            taskController.create(new TaskRequest("과제하기"));
            return taskController;
        }

        @Nested
        class 만약_존재하지_않는_id가_주어진다면 {
            TaskController taskController = taskController();
            Long id = 100L;

            @Test
            void NotFoundException_예외를_발생시킨다() {
                assertThatThrownBy(() -> taskController.delete(id))
                        .isInstanceOf(NotFoundException.class)
                        .hasMessageContaining("Task not found: " + id);
            }
        }

        @Nested
        class 만약_값이_1인_id가_주어진다면 {
            TaskController taskController = taskController();
            Long id = 1L;

            @Test
            void 해당_Task를_삭제한다() {
                assertThat(taskController.getTasks()).as("delete 전 Task 리스트의 크기는 1이다").hasSize(1);
                taskController.delete(id);
                assertThat(taskController.getTasks()).as("delete 후 Task 리스트의 크기는 0이다").hasSize(0);
            }
        }
    }
}
