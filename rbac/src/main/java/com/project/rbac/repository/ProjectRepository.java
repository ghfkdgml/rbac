package com.project.rbac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.rbac.entity.Project;
import java.util.Optional;


public interface ProjectRepository extends JpaRepository<Project, Long> {
    int countByOwnerId(String ownerId);
    Optional<Project> findByIdAndOwnerId(Long id, String ownerId);
    boolean existsByOwnerIdAndTitle(String ownerId, String title);
}
