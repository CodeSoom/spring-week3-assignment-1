package com.codesoom.assignment.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class TaskDto {

    private Long id;
    private String title;
    @JsonIgnore private LocalDateTime regDate;
    @JsonIgnore private LocalDateTime modDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deadLine = LocalDateTime.MAX; // 생성자에서 deadLine을 null로 주게 되면 deadLine이 없는 것으로 간주

    public TaskDto(@JsonProperty String title, @JsonProperty LocalDateTime deadLine) {
        this.title = title;

        if (deadLine == null) {
            return;
        }
        this.deadLine = deadLine;
    }

    private TaskDto() {
    }

    public static TaskDto from(Task task) {
        final TaskDto dto = new TaskDto();
        dto.id = task.getId();
        dto.title = task.getTitle();
        dto.regDate = task.getRegDate();
        dto.modDate = task.getModDate();
        dto.deadLine = task.getDeadLine();
        return dto;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getRegDate() {
        return regDate;
    }

    public LocalDateTime getModDate() {
        return modDate;
    }

    public LocalDateTime getDeadLine() {
        return deadLine;
    }
}
