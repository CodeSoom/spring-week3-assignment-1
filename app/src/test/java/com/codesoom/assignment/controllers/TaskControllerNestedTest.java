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
        class Context_NotEmptyTask{

            @Test
            @DisplayName("모든 Task를 반환한다")
            void It_ReturnAllTask(){
                setDefaultSizeTasks();
                assertThat(controller.list()).hasSize((int) DEFAULT_SIZE);
                for(long i = 1 ; i <= DEFAULT_SIZE ; i++){
                    assertThat(controller.detail(i)).isEqualTo(new Task(i , DEFAULT_TITLE + i));
                }
            }
        }

        @Nested
        @DisplayName("Task가 존재하지 않는다면")
        class Context_EmptyTask{

            @Test
            @DisplayName("비어있는 List를 반환한다")
            void It_ReturnEmptyList(){
                assertThat(controller.list()).hasSize(0);
            }
        }

    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_Detail{

        @Nested
        @DisplayName("id가 null이라면")
        class Context_NullPathVariable{

            @Test
            @DisplayName("TaskNotFoundException이 발생한다")
            void It_ThrowException(){
                assertThatThrownBy(() -> controller.detail(null))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id로 조회한다면")
        class Context_SearchInvalidId{

            @Test
            @DisplayName("TaskNotFoundException이 발생한다")
            void It_ThrowException(){
                assertThatThrownBy(() -> controller.detail(100L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("존재하는 id로 조회한다면")
        class Context_SearchValidId{

            @Test
            @DisplayName("Task를 반환한다")
            void It_ReturnTask(){
                setDefaultSizeTasks();
                assertThat(controller.detail(DEFAULT_SIZE)).isEqualTo(new Task(DEFAULT_SIZE , DEFAULT_TITLE + DEFAULT_SIZE));
            }
        }
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_Create{

        @Nested
        @DisplayName("id가 null이 아닌 Task가 주어져도")
        class Context_NotNullTask{

            @Test
            @DisplayName("generate된 id가 세팅된다")
            void It_GenerateId(){
                long beforeId = DEFAULT_SIZE + 1;
                String title = DEFAULT_TITLE + beforeId;
                Task beforeTask = new Task(beforeId , title);
                Task afterTask = controller.create(beforeTask);
                assertThat(beforeTask.getId()).isNotEqualTo(afterTask.getId());
                assertThat(afterTask.getTitle()).isEqualTo(title);
            }
        }

        @Nested
        @DisplayName("title이 비어있으면")
        class Context_NullTitle{

            @Test
            @DisplayName("Task의 제목은 Null로 저장된다")
            void It_SaveTitleNull(){
                Task saveTask = controller.create(new Task());
                assertThat(saveTask.getTitle()).isEqualTo(null);
            }
        }

        @Nested
        @DisplayName("Task를 전달하면")
        class Context_SendTask{

            @Test
            @DisplayName("저장된다")
            void It_SaveTask(){
                int oldSize = controller.list().size();
                String title = DEFAULT_TITLE + DEFAULT_SIZE;
                Task beforeTask = new Task(null , title);
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

            @Test
            @DisplayName("TaskNotFouneException이 발생한다")
            void It_ThrowException(){
                assertThatThrownBy(() -> controller.update(null , new Task()))
                        .isInstanceOf(TaskNotFoundException.class);
                assertThatThrownBy(() -> controller.update(100L , new Task()))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("id에 해당하는 Task가 있으면")
        class Context_ValidId{

            @Test
            @DisplayName("Task의 title을 업데이트한다")
            void It_UpdateTitle(){
                setDefaultSizeTasks();
                int oldSize = controller.list().size();
                Task beforeTask = new Task(null , "Update Title");
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

            @Test
            @DisplayName("TaskNotFouneException이 발생한다")
            void It_ThrowException(){
                assertThatThrownBy(() -> controller.patch(null , new Task()))
                        .isInstanceOf(TaskNotFoundException.class);
                assertThatThrownBy(() -> controller.patch(100L , new Task()))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("id에 해당하는 Task가 있으면")
        class Context_ValidId{

            @Test
            @DisplayName("Task의 title을 업데이트한다")
            void It_UpdateTitle(){
                setDefaultSizeTasks();
                int oldSize = controller.list().size();
                Task beforeTask = new Task(null , "Update Title");
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

            @Test
            @DisplayName("TaskNotFouneException이 발생한다")
            void It_ThrowException(){
                assertThatThrownBy(() -> controller.delete(null))
                        .isInstanceOf(TaskNotFoundException.class);
                assertThatThrownBy(() -> controller.delete(100L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("id에 해당하는 Task가 있다면")
        class Context_ValidId{

            @Test
            @DisplayName("해당 Task를 삭제한다")
            void It_DeleteTask(){
                setDefaultSizeTasks();
                int beforeSize = controller.list().size();
                controller.delete(DEFAULT_SIZE);
                assertThat(controller.list()).hasSize(beforeSize - 1);
            }
        }
    }
}
