package com.hemanth.taskboard.controller;

import com.hemanth.taskboard.dto.TaskDto;
import com.hemanth.taskboard.model.Task;
import com.hemanth.taskboard.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<Page<TaskDto.Response>> getTasks(
            Authentication authentication,
            @RequestParam(required = false) Task.TaskStatus status,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        Page<TaskDto.Response> tasks = taskService.getTasks(authentication.getName(), status, pageable);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto.Response> getTask(Authentication authentication, @PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTask(authentication.getName(), id));
    }

    @PostMapping
    public ResponseEntity<TaskDto.Response> createTask(
            Authentication authentication,
            @Valid @RequestBody TaskDto.Request request) {
        TaskDto.Response created = taskService.createTask(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto.Response> updateTask(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody TaskDto.Request request) {
        return ResponseEntity.ok(taskService.updateTask(authentication.getName(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(Authentication authentication, @PathVariable Long id) {
        taskService.deleteTask(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats(Authentication authentication) {
        String username = authentication.getName();
        Map<String, Long> stats = Map.of(
                "todo", taskService.countByStatus(username, Task.TaskStatus.TODO),
                "inProgress", taskService.countByStatus(username, Task.TaskStatus.IN_PROGRESS),
                "done", taskService.countByStatus(username, Task.TaskStatus.DONE)
        );
        return ResponseEntity.ok(stats);
    }
}
