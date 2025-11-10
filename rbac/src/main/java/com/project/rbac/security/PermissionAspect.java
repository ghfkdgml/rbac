package com.project.rbac.security;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.project.rbac.common.ApiException;
import com.project.rbac.domain.Action;
import com.project.rbac.repository.ProjectRepository;


@Aspect
@Component
public class PermissionAspect {
    private final ProjectRepository projectRepository;
    public PermissionAspect(ProjectRepository projectRepository) { this.projectRepository = projectRepository; }


    @Around("@annotation(req)")
    public Object check(ProceedingJoinPoint pjp, RequirePermission req) throws Throwable {
        var ctx = RequestContext.get();
        if (ctx == null) throw new ApiException(HttpStatus.UNAUTHORIZED, "No request context");


        // 1) RBAC
        if (!PermissionMatrix.allows(ctx.role(), req.value())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Role " + ctx.role() + " not allowed for " + req.value());
        }

        // 2) Plan limit (only for CREATE)
        if (req.value() == Action.CREATE) {
            int owned = projectRepository.countByOwnerId(ctx.userId());
            int max = PlanLimitPolicy.maxProjects(ctx.plan());
            if (owned >= max) {
                throw new ApiException(HttpStatus.FORBIDDEN, "Plan limit exceeded: " + owned + "/" + max);
            }
        }
        return pjp.proceed();
    }
}
