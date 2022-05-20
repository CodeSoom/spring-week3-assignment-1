package com.codesoom.assignment.application;

import com.codesoom.assignment.dto.TaskRequest;
import com.codesoom.assignment.exceptions.BadRequestException;
import com.codesoom.assignment.exceptions.NotFoundException;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskServiceTest 클래스")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TaskServiceTest {
    @Nested
    class getTasks_메서드는 {
        @Nested
        class 만약_Task가_추가되지_않은_상태라면 {
            TaskService taskService() {
                return new TaskService();
            }

            @Test
            void 크기가_0인_TaskResponse_리스트를_반환한다() {
                assertThat(taskService().getTasks()).hasSize(0);
            }
        }

        @Nested
        class 만약_Task가_1개_추가된_상태라면 {
            TaskService taskService() {
                TaskService taskService = new TaskService();
                taskService.addTask(new TaskRequest("과제하기"));
                return taskService;
            }

            @Test
            void 크기가_1인_TaskResponse_리스트를_반환한다() {
                assertThat(taskService().getTasks()).hasSize(1);
            }
        }
    }

    @Nested
    class getTask_메서드는 {
        TaskService taskService() {
            TaskService taskService = new TaskService();
            taskService.addTask(new TaskRequest("과제하기"));
            return taskService;
        }

        @Nested
        class 만약_값이_1인_id가_주어진다면 {
            TaskService taskService = taskService();
            Long id = 1L;

            @Test
            void id가_1인_TaskResponse_객체를_반환한다() {
                assertThat(taskService.getTask(id).getId()).isEqualTo(1L);
            }
        }

        @Nested
        class 만약_존재하지_않는_id가_주어진다면 {
            TaskService taskService = taskService();
            Long id = 100L;

            @Test
            void NotFoundException_예외를_발생시킨다() {
                assertThatThrownBy(() -> taskService.getTask(id))
                        .isInstanceOf(NotFoundException.class)
                        .hasMessageContaining("Task not found: " + id);
            }
        }
    }

    @Nested
    class findTask_메서드는 {
        TaskService taskService() {
            TaskService taskService = new TaskService();
            taskService.addTask(new TaskRequest("과제하기"));
            return taskService;
        }

        @Nested
        class 만약_값이_1인_id가_주어진다면 {
            TaskService taskService = taskService();
            Long id = 1L;

            @Test
            void id가_1인_Task_객체를_반환한다() {
                assertThat(taskService.findTask(id).getId()).isEqualTo(1L);
            }
        }

        @Nested
        class 만약_존재하지_않는_id가_주어진다면 {
            TaskService taskService = taskService();
            Long id = 100L;

            @Test
            void NotFoundException_예외를_발생시킨다() {
                assertThatThrownBy(() -> taskService.findTask(id))
                        .isInstanceOf(NotFoundException.class)
                        .hasMessageContaining("Task not found: " + id);
            }
        }
    }

    @Nested
    class addTask_메서드는 {
        TaskService taskService() {
            return new TaskService();
        }

        @Nested
        class 만약_title이_빈문자열인_TaskRequest가_주어진다면 {
            TaskService taskService = taskService();
            TaskRequest taskRequest() {
                return new TaskRequest("");
            }

            @Test
            void BadRequestException_예외를_발생시킨다() {
                assertThatThrownBy(() -> taskService.addTask(taskRequest()))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessageContaining("Bad Request");
            }
        }

        @Nested
        class 만약_title이_과제하기인_TaskRequest가_주어진다면 {
            TaskService taskService = taskService();
            TaskRequest taskRequest() {
                return new TaskRequest("과제하기");
            }

            @Test
            void title이_과제하기인_TaskResponse를_반환한다() {
                assertThat(taskService.addTask(taskRequest()).getTitle()).isEqualTo("과제하기");
            }
        }
    }

    @Nested
    class updateTask_메서드는 {
        TaskService taskService() {
            TaskService taskService = new TaskService();
            taskService.addTask(new TaskRequest("과제하기"));
            return taskService;
        }

        @Nested
        class 만약_존재하지_않는_id가_주어진다면 {
            TaskService taskService = taskService();
            Long id = 100L;
            TaskRequest taskRequest() {
                return new TaskRequest("밥먹기");
            }

            @Test
            void NotFoundException_예외를_발생시킨다() {
                assertThatThrownBy(() -> taskService.updateTask(id, taskRequest()))
                        .isInstanceOf(NotFoundException.class)
                        .hasMessageContaining("Task not found: " + id);
            }
        }

        @Nested
        class 만약_title이_빈문자열인_TaskRequest가_주어진다면 {
            TaskService taskService = taskService();
            Long id = 1L;
            TaskRequest taskRequest() {
                return new TaskRequest("");
            }

            @Test
            void BadRequestException_예외를_발생시킨다() {
                assertThatThrownBy(() -> taskService.updateTask(id, taskRequest()))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessageContaining("Bad Request");
            }
        }

        @Nested
        class 만약_title이_밥먹기인_TaskRequest가_주어진다면 {
            TaskService taskService = taskService();
            Long id = 1L;
            TaskRequest taskRequest() {
                return new TaskRequest("밥먹기");
            }

            @Test
            void 해당_Task의_title을_밥먹기로_수정_후_TaskResponse를_반환한다() {
                assertThat(taskService.findTask(id).getTitle()).as("update 전 Task의 title은 과제하기이다").isEqualTo("과제하기");
                assertThat(taskService.updateTask(id, taskRequest()).getTitle()).as("반환되는 TaskResponse의 title은 밥먹기이다").isEqualTo("밥먹기");
                assertThat(taskService.findTask(id).getTitle()).as("update 후 Task의 title은 밥먹기이다").isEqualTo("밥먹기");
            }
        }
    }

    @Nested
    class deleteTask_메서드는 {
        TaskService taskService() {
            TaskService taskService = new TaskService();
            taskService.addTask(new TaskRequest("과제하기"));
            return taskService;
        }

        @Nested
        class 만약_존재하지_않는_id가_주어진다면 {
            TaskService taskService = taskService();
            Long id = 100L;

            @Test
            void NotFoundException_예외를_발생시킨다() {
                assertThatThrownBy(() -> taskService.deleteTask(id))
                        .isInstanceOf(NotFoundException.class)
                        .hasMessageContaining("Task not found: " + id);
            }
        }

        @Nested
        class 만약_값이_1인_id가_주어진다면 {
            TaskService taskService = taskService();
            Long id = 1L;

            @Test
            void 해당_Task를_삭제한다() {
                assertThat(taskService.getTasks()).as("delete 전 Task 리스트의 크기는 1이다").hasSize(1);
                taskService.deleteTask(id);
                assertThat(taskService.getTasks()).as("delete 후 Task 리스트의 크기는 0이다").hasSize(0);
            }
        }
    }

    @Nested
    class generateId_메서드는 {
        @Test
        void newId의_값을_1_증가시킨다() throws NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
            TaskService taskService = new TaskService();
            Method method = taskService.getClass().getDeclaredMethod("generateId", null);
            method.setAccessible(true);

            Field field = taskService.getClass().getDeclaredField("newId");
            field.setAccessible(true);
            Long oldId = (Long) field.get(taskService);

            assertThat(oldId).as("증가 전 id의 값은 0이다").isEqualTo(0L);

            method.invoke(taskService);

            Long newId = (Long) field.get(taskService);

            assertThat(newId).as("증가 후 id의 값은 1이다").isEqualTo(1L);
        }
    }
}
