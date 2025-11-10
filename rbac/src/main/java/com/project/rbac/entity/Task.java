package com.project.rbac.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "tasks")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Task {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Project project;


    @Column(nullable = false)
    private String title;


    @Column(nullable = false)
    private String status; // 예: NEW/IN_PROGRESS/DONE (간단화)
}
