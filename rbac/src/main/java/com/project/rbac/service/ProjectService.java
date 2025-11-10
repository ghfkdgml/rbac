package com.project.rbac.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.rbac.common.ApiException;
import com.project.rbac.domain.ProjectStatus;
import com.project.rbac.dto.ProjectDtos.CreateRequest;
import com.project.rbac.dto.ProjectDtos.UpdateRequest;
import com.project.rbac.entity.Project;
import com.project.rbac.repository.ProjectRepository;
import com.project.rbac.repository.TaskRepository;
import com.project.rbac.security.RequestContext;


@Service
@Transactional
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;


    public ProjectService(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository; this.taskRepository = taskRepository;
    }


    public Project create(CreateRequest req) {
        var ctx = RequestContext.get();
        if (projectRepository.existsByOwnerIdAndTitle(ctx.userId(), req.title())) {
            throw new ApiException(HttpStatus.CONFLICT, "Duplicate title for owner");
        }
        Project p = Project.builder()
            .ownerId(ctx.userId())
            .title(req.title())
            .manager(req.manager())
            .dueDate(req.dueDate())
            .status(ProjectStatus.NEW)
            .build();
        return projectRepository.save(p);
    }


    @Transactional(readOnly = true)
    public Project getOrThrow(Long id) {
        return projectRepository.findById(id)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Project not found"));
    }


    @Transactional(readOnly = true)
    public long countInProgressTasks(Long projectId) {
        return taskRepository.countByProjectIdAndStatus(projectId, "IN_PROGRESS");
    }


    public Project update(Long id, UpdateRequest req) {
        Project p = getOrThrow(id);
        if (req.dueDate() != null) p.setDueDate(req.dueDate());
        if (req.status() != null) p.setStatus(req.status());
        if (req.manager() != null) p.setManager(req.manager());
        return p;
    }


    public void delete(Long id) {
        Project p = getOrThrow(id);
        projectRepository.delete(p);
    }
}
