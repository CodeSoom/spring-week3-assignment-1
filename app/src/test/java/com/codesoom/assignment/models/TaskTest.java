package com.codesoom.assignment.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    public static final String TEST_TITLE = "title";
    public static final long TEST_ID = 0L;

    @DisplayName("id 지정 안했을 때 getId 호출하면 null 반환")
    @Test
    void givenWithoutSetId_whenGetId_thenNull() {
        // given
        final Task task = new Task();

        // when
        final Long actual = task.getId();

        // then
        assertThat(actual).isNull();
    }

    @DisplayName("id 지정 했을 떄 getId 호출하면 지정한 id 반환")
    @Test
    void givenSetId_whenGetId_thenReturnId() {
        // given
        final Task task = new Task();
        task.setId(1L);

        // when
        final Long actual = task.getId();

        // then
        assertThat(actual).isEqualTo(1L);
    }

    @DisplayName("id 변경했을 때 getId 호출하면 변경한 id 반환")
    @Test
    void givenUpdateId_whenGetId_thenReturnId() {
        // given
        final Long oldId = 1L;
        final Long newId = 2L;
        final Task task = new Task();
        task.setId(oldId);
        task.setId(newId);

        // when
        final Long actual = task.getId();

        // then
        assertThat(actual).isEqualTo(newId);
    }

    @DisplayName("title 지정 안했을 때 getTitle 호출하면 null 반환")
    @Test
    void givenWithoutSetTitle_whenGetTitle_thenNull() {
        // given
        final Task task = new Task();

        // when
        final String actual = task.getTitle();

        // then
        assertThat(actual).isNull();
    }

    @DisplayName("title 지정 하고 때 getTitle 호출하면 지정된 title 반환")
    @Test
    void givenSetTitle_whenGetTitle_thenReturnTitle() {
        // given
        final Task task = new Task();
        task.setTitle(TEST_TITLE);

        // when
        final String actual = task.getTitle();

        // then
        assertThat(actual).isEqualTo(TEST_TITLE);
    }

    @DisplayName("title 업데이트 하고 때 getTitle 호출하면 업데이트한 title 반환")
    @Test
    void givenUpdateTitle_whenGetTitle_thenReturnTitle() {
        // given
        final Task task = new Task();
        task.setTitle(TEST_TITLE);
        task.setTitle(TEST_TITLE + "new");

        // when
        final String actual = task.getTitle();

        // then
        assertThat(actual).isEqualTo(TEST_TITLE + "new");
    }

    @DisplayName("id, title 지정하고 getId, getTitle 호출하면 id와 title 반환")
    @Test
    void givenSetIdAndSetTitle_whenGetIdAndGetTitle_thenReturnIdAndTitle() {
        // given
        final Task task = new Task();
        task.setId(TEST_ID);
        task.setTitle(TEST_TITLE);

        // when
        final String title = task.getTitle();
        final Long id = task.getId();

        // then
        assertThat(id).isEqualTo(TEST_ID);
        assertThat(title).isEqualTo(TEST_TITLE);
    }
}
