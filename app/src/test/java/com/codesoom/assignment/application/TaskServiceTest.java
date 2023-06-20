package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Describe - Context - It 패턴
 */
class TaskServiceTest {

	TaskService taskService;
	private final long SET_ID = 1L;

	@BeforeEach
	void setUp() {
		taskService = new TaskService();

		createTaskWithId(SET_ID);
	}

	@Nested
	@DisplayName("getTask 메서드")
	class Describe_getTask {
//		long id = 10L;

		@Nested
		@DisplayName("task가 존재한다면")
		class Context_when_task_exists {
			@Test
			@DisplayName("task를 반환한다.")
			void it_returns_task() {
				assertThat(getTask(SET_ID)).isNotNull();
			}
		}

		@Nested
		@DisplayName("task가 존재하지 않는다면")
		class Context_when_tasks_not_exist {

			@BeforeEach
			void setUpCondition() {
				deleteTask(SET_ID);
			}

			@Test
			@DisplayName("TaskNotFoundException가 발생한다.")
			void it_returns_exeption() {
				assertThatThrownBy(() -> getTask(SET_ID)).isInstanceOf(TaskNotFoundException.class);
			}
		}
	}

	@Nested
	@DisplayName("deleteTask 메서드")
	class Describe_deleteTask {
		@Nested
		@DisplayName("task가 존재한다면")
		class Context_when_task_exists {

			@Test
			@DisplayName("task가 삭제된다.")
			void it_deleted() throws Exception {
				deleteTask(SET_ID);

				assertThatThrownBy(() -> getTask(SET_ID)).isInstanceOf(TaskNotFoundException.class);
			}
		}

		@Nested
		@DisplayName("task가 존재하지 않는다면")
		class Context_when_task_not_exists {

			@BeforeEach
			void setUpCondition() {
				deleteTask(SET_ID);
			}

			@Test
			@DisplayName("TaskNotFoundException가 발생한다.")
			void it_returns() {
				assertThatThrownBy(() -> deleteTask(SET_ID)).isInstanceOf(TaskNotFoundException.class);
			}
		}
	}

	@Test
	void getTasks() {
		assertThat(taskService.getTasks()).hasSize(1);
	}


	@Test
	void createTask() {
		String addTaskTitle = "ADD TASK";
		Task task = Task.title(addTaskTitle);
		int before = taskService.getTasks().size();

		Task created = taskService.createTask(task);
		int after = taskService.getTasks().size();

		assertThat(created.getTitle()).isEqualTo(addTaskTitle);
		assertThat(after - before).isEqualTo(1);
	}

	@Test
	void updateTask() {
		String updateTitle = "UPDATED TASK";
		long id = 1L;
		Task updateTask = Task.title(updateTitle);

		Task task = taskService.updateTask(id, updateTask);

		assertThat(task.getTitle()).isEqualTo(updateTitle);
	}

	private void createTaskWithId(long id) {
		Task task = Task.title("NEW TASK");
		task.setId(id);

		taskService.addTask(task);
	}

	private void deleteTask(long id) {
		taskService.deleteTask(id);
	}

	private Task getTask(long id) {
		return taskService.getTask(id);
	}
}
