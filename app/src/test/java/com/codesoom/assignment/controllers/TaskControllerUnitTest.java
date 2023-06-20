package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * verify() : taskService 를 mock으로 만들어서 verify()를 통해 해덩 서비스의 특정 메서드의 실행여부를 확인할 수 있다.
 * spy : spy 객체는 실제 객체와 같다. 해당 객체의 행동을 그대로 사용 가능하다.
 * mock : mock은 가짜 객체로, 인스턴스 타입만 따라 간다고 보면 된다. 실제 행동을 하는게 아니기 때문에, 행동에 의한 기대 결과를 지정해주어야 한다. - given()
 */
class TaskControllerUnitTest {

	private TaskController taskController;

	private TaskService taskService;

	private static final String TEST_TITLE = "TEST1";
	private static final String UPDATE_POSTFIX = "_!!!";


	@BeforeEach
	public void setUp() {
		taskService = spy(new TaskService());	// proxy 객체를 만듬.
		taskController = new TaskController(taskService);

		Task task = new Task();
		task.setTitle(TEST_TITLE);
		taskController.create(task);
	}

	@Test
	public void getList() {
		assertThat(taskController.list()).hasSize(1);

		verify(taskService).getTasks();
	}

	@Test
	public void getTaskWithInvalidId() {
		Long id = 99L;
		assertThatThrownBy(() -> taskController.detail(id)).isInstanceOf(TaskNotFoundException.class);

		verify(taskService).getTask(any());
	}

	@Test
	public void getTaskWithValidId() {
		assertThat(taskController.detail(1L).getTitle()).isEqualTo(TEST_TITLE);
	}

	@Test
	public void createTask() {
		int before = taskController.list().size();
		taskController.create(new Task());
		int after = taskController.list().size();

		assertThat(after - before).isEqualTo(1);

		verify(taskService, times(2)).createTask(any(Task.class));	// times() 해당 메서드의 실행 횟수 지정.
	}

	@Test
	public void updateWithInvalidId() {
		Long id = 99L;
		Task task = new Task();
		task.setTitle(TEST_TITLE + UPDATE_POSTFIX);

		assertThatThrownBy(() -> taskController.update(id, task)).isInstanceOf(TaskNotFoundException.class);

		verify(taskService).updateTask(any(Long.class), any(Task.class));
	}

	@Test
	public void updateWithValidId() {
		Long id = 1L;
		Task task = new Task();
		task.setTitle(TEST_TITLE + UPDATE_POSTFIX);

		Task update = taskController.update(id, task);

		assertThat(update.getTitle()).isEqualTo(TEST_TITLE + UPDATE_POSTFIX);

		verify(taskService).updateTask(eq(id), any(Task.class));

	}

	@Test
	public void patchWithInvalidId() {
		Long id = 99L;
		Task task = new Task();
		task.setTitle(TEST_TITLE + UPDATE_POSTFIX);

		assertThatThrownBy(() -> taskController.patch(id, task)).isInstanceOf(TaskNotFoundException.class);
	}

	@Test
	public void patchWithValidId() {
		Long id = 1L;
		Task task = new Task();
		task.setTitle(TEST_TITLE + UPDATE_POSTFIX);

		Task update = taskController.patch(id, task);

		assertThat(update.getTitle()).isEqualTo(TEST_TITLE + UPDATE_POSTFIX);
	}

	@Test
	public void deleteWithInvalidId() {
		Long id = 99L;

		assertThatThrownBy(() -> taskController.delete(id)).isInstanceOf(TaskNotFoundException.class);
	}

	@Test
	public void deleteWithValidId() {
		Long id = 1L;

		int before = taskController.list().size();
		taskController.delete(id);
		int after = taskController.list().size();

		assertThat(before - after).isEqualTo(1);
		assertThatThrownBy(() -> taskController.detail(id)).isInstanceOf(TaskNotFoundException.class);
	}

}
