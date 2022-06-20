package com.codesoom.assignment.models;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

    Task task;
    private static final Long INITIAL_ID = 1240L;
    private static final String INITIAL_TITLE = "Test Title";
    private static final Long TEST_ID = 5234L;
    private static final String TEST_TITLE = "Changed Title";

    @BeforeEach
    void setup() {
        task = new Task();
        task.setId(INITIAL_ID);
        task.setTitle(INITIAL_TITLE);
    }

    @DisplayName("getId 동작 테스트")
    @Test
    public void getIdTest() throws Exception {
        //given
        //when
        Long receivedId = task.getId();
        //then
        assertThat(receivedId).isEqualTo(INITIAL_ID);
    }
    @DisplayName("getTitle 동작 테스트")
    @Test
    public void getTitleTest() throws Exception {
        //given
        //when
        String receivedTitle = task.getTitle();
        //then
        assertThat(receivedTitle).isEqualTo(INITIAL_TITLE);
    }
    @DisplayName("setId 동작 테스트")
    @Test
    public void setIdTest() throws Exception {
        //given
        //when
        task.setId(TEST_ID);
        //then
        assertThat(task.getId()).isEqualTo(TEST_ID);
    }
    @DisplayName("setTitle 동작 테스트")
    @Test
    public void setTitleTest() throws Exception {
        //given
        //when
        task.setTitle(TEST_TITLE);
        //then
        assertThat(task.getTitle()).isEqualTo(TEST_TITLE);
    }

}