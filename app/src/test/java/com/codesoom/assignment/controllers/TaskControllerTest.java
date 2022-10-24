package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskControllerTest {

    private static final String TASK_TITLE = "Task";

    private TaskController controller;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        controller = new TaskController(taskService);
    }

    @Nested
    class WhenEmpty {

        @Test
        @DisplayName("get all tasks")
        void list() {
            assertThat(controller.list()).isEmpty();
        }

        @Test
        @DisplayName("get a task")
        void detail() {
            final Long randomId = getRandomId();

            assertThatThrownBy(() -> controller.detail(randomId))
                    .isInstanceOf(TaskNotFoundException.class);
        }

        @Test
        @DisplayName("post a task")
        void create() {
            final int oldSize = controller.list().size();

            final Task src = new Task();
            src.setTitle(TASK_TITLE);
            assertThatNoException().isThrownBy(() -> controller.create(src));

            final List<Task> taskList = controller.list();

            final int newSize = taskList.size();
            assertThat(newSize - oldSize).isEqualTo(1);

            final Task addedTask = taskList.get(0);
            assertThat(addedTask).isNotEqualTo(src);

            final Long addedTaskId = addedTask.getId();
            assertThat(addedTaskId).isNotNull();
            assertThat(addedTaskId).isGreaterThan(0L);
        }

        @Test
        @DisplayName("update a task")
        void update() {
            final Long randomId = getRandomId();

            final Task task = new Task();
            task.setTitle(TASK_TITLE + "`");

            assertThatThrownBy(() -> controller.update(randomId, task))
                    .isInstanceOf(TaskNotFoundException.class);
        }

        @Test
        @DisplayName("delete a task")
        void delete() {
            final Long randomId = getRandomId();

            assertThatThrownBy(() -> controller.delete(randomId))
                    .isInstanceOf(TaskNotFoundException.class);
        }

        Long getRandomId() {
            long id = new Random().nextLong();
            return id < 0L ? -id : id;
        }
    }

    @Nested
    class WhenNotEmpty {

        @BeforeEach
        void addOneTask() {
            final Task task = new Task();
            task.setTitle(TASK_TITLE);
            controller.create(task);
        }

        @Test
        @DisplayName("get all tasks")
        void list() {
            assertThat(controller.list()).isNotEmpty();
        }

        @Test
        @DisplayName("get a task with invalid id")
        void detailInvalidId() {
            final Long invalidId = getInvalidId();

            assertThatThrownBy(() -> controller.detail(invalidId))
                    .isInstanceOf(TaskNotFoundException.class);
        }

        @Test
        @DisplayName("get a task with valid id")
        void detailValidId() {
            final Long validId = controller.list().get(0).getId();
            assertThatNoException().isThrownBy(() -> controller.detail(validId));
        }

        @Test
        @DisplayName("post a task")
        void create() {
            final int oldSize = controller.list().size();
            final Task oldTask = controller.list().get(0);

            assertThatNoException().isThrownBy(this::addOneTask);

            final int newSize = controller.list().size();
            final Task newTask = controller.list().get(1);

            assertThat(newSize - oldSize).isEqualTo(1L);
            assertThat(newTask.getId() - oldTask.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("update a task with invalid id")
        void updateWithInvalidId() {
            final Task task = new Task();
            task.setTitle(TASK_TITLE + "`");

            final Long invalidId = getInvalidId();

            assertThatThrownBy(() -> controller.update(invalidId, task))
                    .isInstanceOf(TaskNotFoundException.class);
        }

        @Test
        @DisplayName("update a task with valid id")
        void updateWithValidId() {
            final Task task = new Task();
            final String newTitle = TASK_TITLE + "`";
            task.setTitle(newTitle);

            final Long validId = getValidId();

            assertThatNoException().isThrownBy(() -> controller.update(validId, task));

            final Task updatedTask = controller.detail(validId);
            assertThat(updatedTask.getTitle()).isEqualTo(newTitle);
        }

        @Test
        @DisplayName("delete a task with invalid id")
        void deleteWithInvalidId() {
            final Long invalidId = getInvalidId();

            assertThatThrownBy(() -> controller.delete(invalidId))
                    .isInstanceOf(TaskNotFoundException.class);
        }

        @Test
        @DisplayName("delete a task with valid id")
        void deleteWithValidId() {
            final Long validId = getValidId();

            assertThatNoException().isThrownBy(() -> controller.delete(validId));
            assertThat(controller.list()).isEmpty();
        }

        Long getValidId() {
            return controller.list().get(0).getId();
        }

        Long getInvalidId() {
            final Long validId = getValidId();

            Optional<Long> invalidIdOptional = new Random().longs(100).boxed()
                    .filter(l -> !l.equals(validId))
                    .findAny();

            return invalidIdOptional.orElse(Long.MAX_VALUE);
        }
    }
}