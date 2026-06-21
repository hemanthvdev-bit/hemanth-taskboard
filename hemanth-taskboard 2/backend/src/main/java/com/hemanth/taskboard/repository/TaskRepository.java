package com.hemanth.taskboard.repository;

import com.hemanth.taskboard.model.Task;
import com.hemanth.taskboard.model.Task.TaskStatus;
import com.hemanth.taskboard.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByOwner(User owner, Pageable pageable);

    Page<Task> findByOwnerAndStatus(User owner, TaskStatus status, Pageable pageable);

    Optional<Task> findByIdAndOwner(Long id, User owner);

    long countByOwnerAndStatus(User owner, TaskStatus status);
}
