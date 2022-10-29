package com.codesoom.assignment.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    private final Long id;
    private final String title;
    private final LocalDateTime regDate;
    private final LocalDateTime modDate;
    private final LocalDateTime deadLine;

    public static class Builder {
        private Long id;
        private String title;
        private LocalDateTime regDate;
        private LocalDateTime modDate;
        private LocalDateTime deadLine = LocalDateTime.MAX;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder regDate(LocalDateTime regDate) {
            this.regDate = regDate;
            return this;
        }

        public Builder modDate(LocalDateTime modDate) {
            this.modDate = modDate;
            return this;
        }

        public Builder deadLine(LocalDateTime deadLine) {
            this.deadLine = deadLine;
            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private Task(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.regDate = builder.regDate;
        this.modDate = builder.modDate;
        this.deadLine = builder.deadLine;
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

    public Task changeTitle(Task source) {
        return new Builder()
                .id(this.getId())
                .title(source.getTitle())
                .regDate(this.getRegDate())
                .modDate(LocalDateTime.now())
                .deadLine(this.getDeadLine())
                .build();
    }

    public Task changeDeadLine(Task source) {
        return new Builder()
                .id(this.getId())
                .title(this.getTitle())
                .regDate(this.getRegDate())
                .modDate(LocalDateTime.now())
                .deadLine(source.getDeadLine())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;
        return Objects.equals(id, task.id)
                && Objects.equals(title, task.title)
                && Objects.equals(regDate, task.regDate)
                && Objects.equals(modDate, task.modDate)
                && Objects.equals(deadLine, task.deadLine);
    }

}
