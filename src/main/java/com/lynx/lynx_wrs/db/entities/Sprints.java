package com.lynx.lynx_wrs.db.entities;

package com.lynx.lynx_wrs.db.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sprints",
        uniqueConstraints = @UniqueConstraint(columnNames = {"cycle_id", "sprint_count"}),
        indexes = {
                @Index(name = "idx_sprint_start_date", columnList = "start_date"),
                @Index(name = "idx_sprint_end_date", columnList = "end_date")
        })
public class Sprints {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cycle_id", nullable = false)
    private Cycles cycle;

    @Column(nullable = false)
    private Integer sprintCount;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Tasks> tasks = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
