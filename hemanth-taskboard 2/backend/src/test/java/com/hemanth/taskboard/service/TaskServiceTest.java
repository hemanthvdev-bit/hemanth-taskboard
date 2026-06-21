package com.hemanth.taskboard.service;

import com.hemanth.taskboard.dto.TaskDto;
import com.hemanth.taskboard.exception.ResourceNotFoundException;
import com.hemanth.taskboard.model.Task;
import com.hemanth.taskboard.model.User;
import com.hemanth.taskboard.repository.TaskRepository;
import com.hemanth.taskboard.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("hemanth")
                .email("hemanth@example.com")
                .password("encoded")
                .role(User.Role.USER)
                .build();
    }

    @Test
    void createTask_shouldSaveAndReturnResponse() {
        when(userRepository.findByUsername("hemanth")).thenReturn(Optional.of(testUser));

        TaskDto.Request request = new TaskDto.Request();
        request.setTitle("Set up CI pipeline");
        request.setDescription("Add GitHub Actions workflow");
        request.setStatus(Task.TaskStatus.TODO);
        request.setPriority(Task.TaskPriority.HIGH);

        Task savedTask = Task.builder()
                .id(10L)
                .title(request.getTitle())
                .description(request.getDescription())
                .status(Task.TaskStatus.TODO)
                .priority(Task.TaskPriority.HIGH)
                .owner(testUser)
                .build();

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskDto.Response response = taskService.createTask("hemanth", request);

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getTitle()).isEqualTo("Set up CI pipeline");
        assertThat(response.getPriority()).isEqualTo(Task.TaskPriority.HIGH);
    }

    @Test
    void getTask_whenNotFound_shouldThrowException() {
        when(userRepository.findByUsername("hemanth")).thenReturn(Optional.of(testUser));
        when(taskRepository.findByIdAndOwner(99L, testUser)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTask("hemanth", 99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void deleteTask_whenOwnedByUser_shouldDelete() {
        Task task = Task.builder().id(5L).title("Old task").owner(testUser).build();
        when(userRepository.findByUsername("hemanth")).thenReturn(Optional.of(testUser));
        when(taskRepository.findByIdAndOwner(5L, testUser)).thenReturn(Optional.of(task));

        taskService.deleteTask("hemanth", 5L);

        org.mockito.Mockito.verify(taskRepository).delete(task);
    }
}
