package com.lynx.lynx_wrs.db.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task_activity",
        indexes = @Index(name = "idx_task_activity_time", columnList = "task_id, occurred_at"))
public class TaskActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Tasks task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id", nullable = false)
    private Users actor;

    @Enumerated(EnumType.STRING)
    private TaskStatus fromStatus;

    @Enumerated(EnumType.STRING)
    private TaskStatus toStatus;

    @Column(nullable = false)
    private LocalDateTime occurredAt = LocalDateTime.now();
}
