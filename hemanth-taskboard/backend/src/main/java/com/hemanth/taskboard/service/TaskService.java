package com.hemanth.taskboard.service;

import com.hemanth.taskboard.dto.TaskDto;
import com.hemanth.taskboard.exception.ResourceNotFoundException;
import com.hemanth.taskboard.model.Task;
import com.hemanth.taskboard.model.User;
import com.hemanth.taskboard.repository.TaskRepository;
import com.hemanth.taskboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<TaskDto.Response> getTasks(String username, Task.TaskStatus status, Pageable pageable) {
        User owner = getUser(username);
        Page<Task> tasks = status != null
                ? taskRepository.findByOwnerAndStatus(owner, status, pageable)
                : taskRepository.findByOwner(owner, pageable);
        return tasks.map(TaskDto.Response::fromEntity);
    }

    @Transactional(readOnly = true)
    public TaskDto.Response getTask(String username, Long taskId) {
        User owner = getUser(username);
        Task task = taskRepository.findByIdAndOwner(taskId, owner)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));
        return TaskDto.Response.fromEntity(task);
    }

    @Transactional
    public TaskDto.Response createTask(String username, TaskDto.Request request) {
        User owner = getUser(username);

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus() != null ? request.getStatus() : Task.TaskStatus.TODO)
                .priority(request.getPriority() != null ? request.getPriority() : Task.TaskPriority.MEDIUM)
                .dueDate(request.getDueDate())
                .owner(owner)
                .build();

        Task saved = taskRepository.save(task);
        return TaskDto.Response.fromEntity(saved);
    }

    @Transactional
    public TaskDto.Response updateTask(String username, Long taskId, TaskDto.Request request) {
        User owner = getUser(username);
        Task task = taskRepository.findByIdAndOwner(taskId, owner)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());
        if (request.getPriority() != null) task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());

        Task saved = taskRepository.save(task);
        return TaskDto.Response.fromEntity(saved);
    }

    @Transactional
    public void deleteTask(String username, Long taskId) {
        User owner = getUser(username);
        Task task = taskRepository.findByIdAndOwner(taskId, owner)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));
        taskRepository.delete(task);
    }

    @Transactional(readOnly = true)
    public long countByStatus(String username, Task.TaskStatus status) {
        User owner = getUser(username);
        return taskRepository.countByOwnerAndStatus(owner, status);
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }
}
