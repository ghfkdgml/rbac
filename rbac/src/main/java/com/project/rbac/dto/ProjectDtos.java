package com.project.rbac.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import com.project.rbac.domain.ProjectStatus;
import com.project.rbac.entity.Project;

public class ProjectDtos {
    public record CreateRequest(
        @NotBlank String title,
        String manager,
        LocalDate dueDate
    ) { }


    public record UpdateRequest(
        LocalDate dueDate,
        ProjectStatus status,
        String manager
    ) { }


    public record DetailResponse(
        Long id,
        String ownerId,
        String title,
        String manager,
        LocalDate dueDate,
        ProjectStatus status,
        long inProgressTaskCount
    ) {
            public static DetailResponse from(Project p, long inProgressCount) {
            return new DetailResponse(p.getId(), p.getOwnerId(), p.getTitle(), p.getManager(), p.getDueDate(), p.getStatus(), inProgressCount);
        }
    }
}
