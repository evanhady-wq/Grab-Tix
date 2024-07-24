package com.grabtix.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.DataTruncation;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "m_event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String category;
    private String description;
    private String location;
    private Date date;
    private Integer vipQuota;
    private Integer regularQuota;
    private double vipTicketPrice;
    private double regularTicketPrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "creator_id")
    private Creator eventCreator;
    private Boolean isDone;

    @PrePersist
    protected void onCreate() {
        isDone = Boolean.FALSE;
    }

    public boolean hasAvailableTickets(Ticket.Type type, int quantity) {
        if (type == Ticket.Type.VIP) {
            return vipQuota >= quantity;
        } else {
            return regularQuota >= quantity;
        }
    }

    public void reduceQuota(Ticket.Type type, int quantity) {
        if (type == Ticket.Type.VIP) {
            this.vipQuota -= quantity;
        } else {
            this.regularQuota -= quantity;
        }
    }

    public void increaseQuota(Ticket.Type type){
        if (type == Ticket.Type.VIP) {
            this.vipQuota += 1;
        } else {
            this.regularQuota += 1;
        }
    }
}
