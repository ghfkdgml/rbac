package com.project.rbac.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.rbac.common.ApiResponse;
import com.project.rbac.domain.Action;
import com.project.rbac.dto.ProjectDtos.CreateRequest;
import com.project.rbac.dto.ProjectDtos.DetailResponse;
import com.project.rbac.dto.ProjectDtos.UpdateRequest;
import com.project.rbac.security.RequirePermission;
import com.project.rbac.service.ProjectService;


@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService service;
    public ProjectController(ProjectService service) { this.service = service; }


    @PostMapping
    @RequirePermission(Action.CREATE)
    public ResponseEntity<ApiResponse<DetailResponse>> create(@Valid @RequestBody CreateRequest req) {
        var p = service.create(req);
        long cnt = service.countInProgressTasks(p.getId());
        return ResponseEntity.ok(ApiResponse.ok(DetailResponse.from(p, cnt)));
    }


    @GetMapping("/{id}")
    @RequirePermission(Action.READ)
    public ResponseEntity<ApiResponse<DetailResponse>> get(@PathVariable Long id) {
        var p = service.getOrThrow(id);
        long cnt = service.countInProgressTasks(id);
        return ResponseEntity.ok(ApiResponse.ok(DetailResponse.from(p, cnt)));
    }


    @PatchMapping("/{id}")
    @RequirePermission(Action.UPDATE)
    public ResponseEntity<ApiResponse<DetailResponse>> update(@PathVariable Long id, @Valid @RequestBody UpdateRequest req) {
        var p = service.update(id, req);
        long cnt = service.countInProgressTasks(id);
        return ResponseEntity.ok(ApiResponse.ok(DetailResponse.from(p, cnt)));
    }


    @DeleteMapping("/{id}")
    @RequirePermission(Action.DELETE)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
