package com.project.rbac.security;

import com.project.rbac.domain.*;

public class RequestContext {
    private final String userId;
    private final Role role;
    private final Plan plan;


    public RequestContext(String userId, Role role, Plan plan) {
        this.userId = userId; this.role = role; this.plan = plan;
    }
    public String userId() { return userId; }
    public Role role() { return role; }
    public Plan plan() { return plan; }


    private static final ThreadLocal<RequestContext> HOLDER = new ThreadLocal<>();
    public static void set(RequestContext ctx) { HOLDER.set(ctx); }
    public static RequestContext get() { return HOLDER.get(); }
    public static void clear() { HOLDER.remove(); }
}
