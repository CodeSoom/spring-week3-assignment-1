package com.codesoom.assignment.repository;

import com.codesoom.assignment.models.Task;

import java.util.Collection;
import java.util.Optional;

/**
 * 작업을 관리하는 역할을 가지고 있다.
 */
public interface TaskRepository {
    /**
     * 주어진 식별자를 가진 작업을 리턴한다.
     *
     * @param id 식별자
     * @return 작업
     */
    Optional<Task> get(Long id);

    /**
     * 작업 콜렉션을 리턴한다.
     *
     * @return 작업 콜렉션
     */
    Collection<Task> getAll();

    /**
     * 작업을 추가합니다.
     *
     * @param title 제목
     * @return 작업
     */
    Task add(String title);

    /**
     * 주어진 식별자를 가진 작업을 삭제한다.
     *
     * @param id 식별자
     */
    void remove(Long id);
}
