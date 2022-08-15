package com.codesoom.assignment.application;

import com.codesoom.assignment.models.Task;

import java.util.Collection;

/**
 * 작업에 대한 비즈니스 로직을 처리하는 역할을 가지고 있습니다.
 */
public interface TaskService {
    /**
     * 작업 콜렉션을 리턴합니다.
     *
     * @return 작업 콜렉션
     */
    Collection<Task> getTasks();

    /**
     * 작업을 리턴합니다.
     *
     * @param id 식별자
     * @return 작업
     */
    Task getTask(Long id);

    /**
     * 작업을 생성하고 리턴합니다.
     *
     * @param title 제목
     * @return 작업
     */
    Task createTask(String title);

    /**
     * 주어진 식별자를 가진 작업을 수정하고 리턴합니다.
     *
     * @param id 식별자
     * @param title 변경할 제목
     * @return 작업
     */
    Task updateTask(Long id, String title);

    /**
     * 주어진 식별자를 가진 작업을 제거합니다.
     *
     * @param id 식별자
     */
    void deleteTask(Long id);
}
