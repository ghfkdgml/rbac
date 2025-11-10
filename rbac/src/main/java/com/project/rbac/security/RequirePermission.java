package com.project.rbac.security;

import java.lang.annotation.*;

import com.project.rbac.domain.Action;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission { Action value(); }
