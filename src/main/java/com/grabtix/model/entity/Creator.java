package com.grabtix.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "m_creator")
public class Creator {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String description;
    private String phone;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Boolean isDeleted;

    @PrePersist
    protected void onCreate() {
        isDeleted = Boolean.FALSE;
    }
}
