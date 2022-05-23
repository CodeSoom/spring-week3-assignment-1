package com.codesoom.assignment.application;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;

@Nested
@DisplayName("TaskService 클래스는")
public class TaskServiceTest {
	private static final String TEST_TASK_1_NAME = "test1";
	private static final String TEST_TASK_2_NAME = "test2";
	private TaskService taskService;

	@BeforeEach
	void setUp() {
		taskService = new TaskService();

		Task task1 = new Task();
		task1.setTitle(TEST_TASK_1_NAME);
		taskService.createTask(task1);

		Task task2 = new Task();
		task2.setTitle(TEST_TASK_2_NAME);
		taskService.createTask(task2);
	}

	@Nested
	@DisplayName("getTask method 는")
	class getTask{

		@Nested
		@DisplayName("유효한 ID 를 인자로 받으면")
		class getTaskWithValidId {

			@Test
			@DisplayName("해당 ID 의 Task 를 반환한다")
			void getTaskWithValidId() {
				Task task1 = taskService.getTask(1L);
				Task task2 = taskService.getTask(2L);

				assertThat(task1.getTitle()).isEqualTo("test1");
				assertThat(task2.getTitle()).isEqualTo("test2");
			}
		}

		@Nested
		@DisplayName("유효하지 않은 ID 를 인자로 받으면")
		class getTaskWithInValidId {

			@Test
			@DisplayName("TaskNotFoundException 을 반환한다")
			void getTaskWithInValidId() {
				assertThatThrownBy(() -> taskService.getTask(3L)).isInstanceOf(TaskNotFoundException.class);
			}
		}
	}

	@Nested
	@DisplayName("deleteTask method 는")
	class deleteTask{
		@Nested
		@DisplayName("유효한 ID 를 인자로 받으면")
		class deleteTaskWithValidID {
			@Test
			@DisplayName("해당 ID 의 Task 를 반환한다")
			void deleteTaskWithValidID() {
				assertThat(taskService.deleteTask(1L).getTitle()).isEqualTo("test1");
			}
		}
		@Nested
		@DisplayName("유효하지 않은 ID 를 인자로 받으면")
		class deleteTaskWithInValidID {
			@Test
			@DisplayName("TaskNotFoundException 을 반환한다")
			void deleteTaskWithInValidID() {
				assertThatThrownBy(() -> taskService.getTask(3L)).isInstanceOf(TaskNotFoundException.class);
			}
		}
	}

	@Nested
	@DisplayName("updateTask method 는")
	class updateTask{
		@Nested
		@DisplayName("유효한 ID 와 변경할 title 을 인자로 받으면")
		class updateTaskWithValidID {
			@Test
			@DisplayName("해당 ID 의 title 이 변경된 Task 가 반환한다")
			void updateTaskWithValidID() {
				Task task = new Task();
				task.setTitle("update");
				assertThat(taskService.updateTask(1L,task).getTitle()).isEqualTo("update");
			}
		}
		@Nested
		@DisplayName("유효하지 않은 ID 를 인자로 받으면")
		class updateTaskWithInValidID {
			@Test
			@DisplayName("TaskNotFoundException 을 반환한다")
			void updateTaskWithInValidID() {
				assertThatThrownBy(() -> taskService.deleteTask(3L)).isInstanceOf(TaskNotFoundException.class);
			}
		}
	}

	@Nested
	@DisplayName("getTasks method 는")
	class getTasks{
		@Test
		@DisplayName("task list 를 전부 반환한다")
		void patchTaskWithValidID() {
			assertThat(taskService.getTasks().size()).isEqualTo(2);
		}
	}
}

