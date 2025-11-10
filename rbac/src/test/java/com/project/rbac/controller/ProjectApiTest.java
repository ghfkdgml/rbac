package com.project.rbac.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.rbac.repository.ProjectRepository;
import com.project.rbac.repository.TaskRepository;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class ProjectApiTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @Autowired ProjectRepository projectRepository;
    @Autowired TaskRepository taskRepository;


    private String json(Object o) throws Exception { return om.writeValueAsString(o); }


    private org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder withAuth(
        org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder b,
        String user, String role, String plan) {
        return b.header("X-User-Id", user).header("X-Role", role).header("X-Plan", plan);
    }


    // ─────────────────────────────────────────────────────────────────────────
    // PLAN LIMIT TESTS
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    void create_basic_limit_1() throws Exception {
        var body = "{\"title\":\"p1\"}";

        mvc.perform(withAuth(post("/projects").contentType(MediaType.APPLICATION_JSON).content(body), "u1","A","BASIC"))
            .andExpect(status().isOk());
        // 두 번째 생성은 플랜 한도 초과로 403
        mvc.perform(withAuth(post("/projects").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"p2\"}"), "u1","A","BASIC"))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.error").value(org.hamcrest.Matchers.containsString("Plan limit")));
    }


    @Test
    void create_pro_limit_5() throws Exception {
        for (int i = 1; i <= 5; i++) {
            mvc.perform(withAuth(post("/projects").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"p"+i+"\"}"), "u2","A","PRO"))
                .andExpect(status().isOk());
        }
        
        mvc.perform(withAuth(post("/projects").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"p6\"}"), "u2","A","PRO"))
        .andExpect(status().isForbidden());
    }


    @Test
    void duplicate_title_conflict_per_owner() throws Exception {
        mvc.perform(withAuth(post("/projects").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"dup\"}"), "u3","A","PRO"))
            .andExpect(status().isOk());
        // 같은 owner(u3) + 같은 title이면 409
        mvc.perform(withAuth(post("/projects").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"dup\"}"), "u3","A","PRO"))
            .andExpect(status().isConflict());
        // 다른 owner면 가능
        mvc.perform(withAuth(post("/projects").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"dup\"}"), "u4","A","PRO"))
            .andExpect(status().isOk());
    }


}
