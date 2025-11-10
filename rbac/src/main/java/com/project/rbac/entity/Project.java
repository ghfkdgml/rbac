package com.project.rbac.entity;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;

import com.project.rbac.domain.ProjectStatus;


@Entity
@Table(name = "projects", uniqueConstraints = {@UniqueConstraint(name = "uk_owner_title", columnNames = {"ownerId", "title"})})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Project {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String ownerId; // 요청자(생성자)


    @Column(nullable = false)
    private String title;


    private String manager; // 담당자 userId (옵션)


    private LocalDate dueDate;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status;
}
