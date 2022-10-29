package com.codesoom.assignment.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class TaskDto {

    private Long id;
    private final String title;
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

    private TaskDto(Long id, String title, LocalDateTime regDate, LocalDateTime modDate, LocalDateTime deadLine) {
        this.id = id;
        this.title = title;
        this.regDate = regDate;
        this.modDate = modDate;
        this.deadLine = deadLine;
    }

    public static TaskDto from(Task task) {
        return new TaskDto(task.getId(), task.getTitle(), task.getRegDate(), task.getModDate(), task.getDeadLine());
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
