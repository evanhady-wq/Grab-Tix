package com.grabtix.model.dto.request;

import com.grabtix.model.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketRequest {
    private String id;
    private String eventId;
    private String customerId;
    private Ticket.Type ticketType;
    private Double price;
}
