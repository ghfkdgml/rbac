package com.project.rbac.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.rbac.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    long countByProjectIdAndStatus(Long projectId, String status);
}
