package com.project.rbac.security;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import com.project.rbac.domain.*;


// RBAC 정책 표
public final class PermissionMatrix {
    private static final Map<Role, Set<Action>> MATRIX = new EnumMap<>(Role.class);

    static {
        // 프로젝트 생성: A/B/C/D 모두 가능
        // 수정: A/B만
        // 삭제: A/B/C만
        // 조회: A/D만
        MATRIX.put(Role.A, EnumSet.of(Action.CREATE, Action.READ, Action.UPDATE, Action.DELETE));
        MATRIX.put(Role.B, EnumSet.of(Action.CREATE, Action.UPDATE, Action.DELETE));
        MATRIX.put(Role.C, EnumSet.of(Action.CREATE, Action.DELETE));
        MATRIX.put(Role.D, EnumSet.of(Action.CREATE, Action.READ));
    }
    
    private PermissionMatrix() {}


    public static boolean allows(Role role, Action action) {
    return MATRIX.getOrDefault(role, EnumSet.noneOf(Action.class)).contains(action);
    }
}
