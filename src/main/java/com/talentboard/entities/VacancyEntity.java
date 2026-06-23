package com.talentboard.entities;

import com.talentboard.enums.Categories;
import com.talentboard.enums.Status;
import com.talentboard.enums.WorkModality;
import com.talentboard.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "vacancy")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class VacancyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Categories category;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private WorkModality workModality;

    @Column(columnDefinition = "DOUBLE PRECISION CHECK (salary >= 1.0)")
    private double salary;

    private LocalDate creationDate;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsible_User_id", nullable = false)
    private User responsibleUser;

    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDate.now();
    }

}
