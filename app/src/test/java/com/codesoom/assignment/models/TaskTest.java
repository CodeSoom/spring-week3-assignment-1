package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {
    private Task task;

    @BeforeEach
    void setup() {
        task = new Task(1L, "title");
    }

    @Test
    @DisplayName("test if the updated task has different title but same id")
    void updateTitle(){
        //given
        //when
        Task updatedTitle = task.updateTitle("title2");
        //then
        assertThat(updatedTitle.getTitle()).isNotEqualTo(task.getTitle());
        assertThat(updatedTitle.getId()).isEqualTo(task.getId());
    }

    @Test
    @DisplayName("test if getId() equals to previously defined id")
    void getIdWithValidId(){
        //given
        Long id1 = 1L;
        //when
        Long getId = task.getId();
        //then
        assertThat(id1).isEqualTo(getId);
    }

    @Test
    @DisplayName("test if the getTitle() equals to previously defined title" )
    void getTitleWithValidId(){
        //given
        String title1 = "title";
        //when
        String title2 = task.getTitle();
        //then
        assertThat(title1).isEqualTo(title2);
}
}
