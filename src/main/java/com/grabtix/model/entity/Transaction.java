package com.grabtix.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "m_transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private LocalDate transactionDate;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> ticket;

    private Integer qty;

    private Double totalPrice;

    private Status status;

    private LocalDate paidAt;

    public enum Status {
        PAID,
        UNPAID,
        CANCELLED
    }

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = Status.UNPAID;
            transactionDate = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if (status == Status.PAID) {
            paidAt = LocalDate.now();
        }
    }
}
