package com.hemanth.taskboard.dto;

import com.hemanth.taskboard.model.Task;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

public class TaskDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank
        @Size(max = 150)
        private String title;

        @Size(max = 2000)
        private String description;

        private Task.TaskStatus status;
        private Task.TaskPriority priority;
        private LocalDate dueDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String title;
        private String description;
        private Task.TaskStatus status;
        private Task.TaskPriority priority;
        private LocalDate dueDate;
        private Instant createdAt;
        private Instant updatedAt;

        public static Response fromEntity(Task task) {
            return Response.builder()
                    .id(task.getId())
                    .title(task.getTitle())
                    .description(task.getDescription())
                    .status(task.getStatus())
                    .priority(task.getPriority())
                    .dueDate(task.getDueDate())
                    .createdAt(task.getCreatedAt())
                    .updatedAt(task.getUpdatedAt())
                    .build();
        }
    }
}
