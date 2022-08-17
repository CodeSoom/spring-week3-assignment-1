package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskControllerNestedTest {

    private final String DEFAULT_TITLE = "TEST";
    private final long DEFAULT_SIZE = 3;
    private TaskController controller;

    @BeforeEach
    void setUp() {
        // subject
        controller = new TaskController(new TaskService());

        // fixtures
    }

    void setDefaultSizeTasks(){
        for(long i = 1 ; i <= DEFAULT_SIZE ; i++){
            Task newTask = new Task();
            newTask.setTitle(DEFAULT_TITLE + i);
            controller.create(newTask);
        }
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_List{

        @Nested
        @DisplayName("Task가 존재한다면")
        class Context_ExistsTask{

            @BeforeEach
            void setUp(){
                setDefaultSizeTasks();
            }

            @Test
            @DisplayName("모든 Task를 반환한다")
            void It_ReturnAllTask(){
                assertThat(controller.list()).hasSize((int) DEFAULT_SIZE);
                for(long i = 1 ; i <= DEFAULT_SIZE ; i++){
                    assertThat(controller.detail(i)).isEqualTo(new Task(i , DEFAULT_TITLE + i));
                }
            }
        }

        @Nested
        @DisplayName("Task가 존재하지 않는다면")
        class Context_NotExistsTask{
            private final int EMPTY = 0;

            @Test
            @DisplayName("비어있는 List를 반환한다")
            void It_ReturnEmptyList(){
                assertThat(controller.list()).hasSize(EMPTY);
            }
        }

    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_Detail{

        @Nested
        @DisplayName("id가 null이라면")
        class Context_NullPathVariable{
            private final Long givenId = null;

            @Test
            @DisplayName("TaskNotFoundException를 던진다")
            void It_ThrowException(){
                assertThatThrownBy(() -> controller.detail(givenId))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id로 조회한다면")
        class Context_SearchInvalidId{
            private final Long invalidId = 100L;

            @Test
            @DisplayName("TaskNotFoundException을 던진다")
            void It_ThrowException(){
                assertThatThrownBy(() -> controller.detail(invalidId))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("존재하는 id로 조회한다면")
        class Context_SearchValidId{

            @BeforeEach
            void setUp(){
                setDefaultSizeTasks();
            }

            @Test
            @DisplayName("Task를 반환한다")
            void It_ReturnTask(){
                assertThat(controller.detail(DEFAULT_SIZE))
                        .isEqualTo(new Task(DEFAULT_SIZE , DEFAULT_TITLE + DEFAULT_SIZE));
            }
        }
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_Create{

        @Nested
        @DisplayName("id가 null이 아닌 Task가 주어져도")
        class Context_NotNullIdTask{
            private final long beforeId = DEFAULT_SIZE + 1;
            private final String title = DEFAULT_TITLE + beforeId;
            private final Task beforeTask = new Task(beforeId , title);

            @Test
            @DisplayName("generate된 id가 세팅된다")
            void It_GenerateId(){
                Task afterTask = controller.create(beforeTask);
                assertThat(beforeTask.getId()).isNotEqualTo(afterTask.getId());
                assertThat(afterTask.getTitle()).isEqualTo(title);
            }
        }

        @Nested
        @DisplayName("title이 null이면")
        class Context_NullTitle{
            private final Task titleNullTask = new Task();
            private final String title = null;

            @Test
            @DisplayName("Task의 제목은 null로 저장된다")
            void It_SaveTitleNull(){
                Task saveTask = controller.create(titleNullTask);
                assertThat(saveTask.getTitle()).isEqualTo(title);
            }
        }

        @Nested
        @DisplayName("title이 존재하는 Task를 전달하면")
        class Context_ExistsTitleTask{
            private final String title = DEFAULT_TITLE + DEFAULT_SIZE;
            private final Task beforeTask = new Task(null , title);

            @Test
            @DisplayName("저장된다")
            void It_SaveTask(){
                int oldSize = controller.list().size();
                Task afterTask = controller.create(beforeTask);
                assertThat(beforeTask.getTitle()).isEqualTo(afterTask.getTitle());
                assertThat(controller.list()).hasSize(oldSize + 1);
            }
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_Update{

        @Nested
        @DisplayName("id가 null이거나 id에 해당하는 Task가 없다면")
        class Context_IdIsNullAndInvalidId{
            private final Long nullId = null;
            private final Long invalidId = 100L;
            private final Task updateTask = new Task();

            @Test
            @DisplayName("TaskNotFouneException이 발생한다")
            void It_ThrowException(){
                assertThatThrownBy(() -> controller.update(nullId , updateTask))
                        .isInstanceOf(TaskNotFoundException.class);
                assertThatThrownBy(() -> controller.update(invalidId , updateTask))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("id에 해당하는 Task가 있으면")
        class Context_ValidId{

            private final String title = DEFAULT_TITLE + (DEFAULT_SIZE + 1);
            private final Task beforeTask = new Task(null , title);

            @BeforeEach
            void setUp(){
                setDefaultSizeTasks();
            }

            @Test
            @DisplayName("Task의 title을 업데이트한다")
            void It_UpdateTitle(){
                int oldSize = controller.list().size();
                Task afterTask = controller.update(DEFAULT_SIZE , beforeTask);
                assertThat(beforeTask.getTitle()).isEqualTo(afterTask.getTitle());
                assertThat(controller.list()).hasSize(oldSize);
            }
        }
    }

    @Nested
    @DisplayName("patch 메소드는")
    class Describe_Patch{

        @Nested
        @DisplayName("id가 null이거나 id에 해당하는 Task가 없다면")
        class Context_IdIsNullAndInvalidId{

            private final Long nullId = null;
            private final Long invalidId = 100L;
            private final Task updateTask = new Task();

            @Test
            @DisplayName("TaskNotFouneException이 발생한다")
            void It_ThrowException(){
                assertThatThrownBy(() -> controller.patch(nullId , updateTask))
                        .isInstanceOf(TaskNotFoundException.class);
                assertThatThrownBy(() -> controller.patch(invalidId , updateTask))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("id에 해당하는 Task가 있으면")
        class Context_ValidId{

            private final String title = DEFAULT_TITLE + (DEFAULT_SIZE + 1);
            private final Task beforeTask = new Task(null , title);

            @BeforeEach
            void setUp(){
                setDefaultSizeTasks();
            }

            @Test
            @DisplayName("Task의 title을 업데이트한다")
            void It_UpdateTitle(){
                int oldSize = controller.list().size();
                Task afterTask = controller.patch(DEFAULT_SIZE , beforeTask);
                assertThat(beforeTask.getTitle()).isEqualTo(afterTask.getTitle());
                assertThat(controller.list()).hasSize(oldSize);
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_Delete{

        @Nested
        @DisplayName("id가 null이거나 id에 해당하는 Task가 없다면")
        class Context_IdIsNullAndInvalidId{

            private final Long nullId = null;
            private final Long invalidId = 100L;

            @Test
            @DisplayName("TaskNotFouneException이 발생한다")
            void It_ThrowException(){
                assertThatThrownBy(() -> controller.delete(nullId))
                        .isInstanceOf(TaskNotFoundException.class);
                assertThatThrownBy(() -> controller.delete(invalidId))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("id에 해당하는 Task가 있다면")
        class Context_ValidId{

            private final Long validId = DEFAULT_SIZE;

            @BeforeEach
            void setUp(){
                setDefaultSizeTasks();
            }

            @Test
            @DisplayName("해당 Task를 삭제한다")
            void It_DeleteTask(){
                int beforeSize = controller.list().size();
                controller.delete(validId);
                assertThat(controller.list()).hasSize(beforeSize - 1);
            }
        }
    }
}
