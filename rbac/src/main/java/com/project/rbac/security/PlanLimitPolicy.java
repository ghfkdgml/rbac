package com.project.rbac.security;

import com.project.rbac.domain.Plan;

public class PlanLimitPolicy {
    private PlanLimitPolicy() {}
    public static int maxProjects(Plan plan) { return plan == Plan.BASIC ? 1 : 5; }
}
