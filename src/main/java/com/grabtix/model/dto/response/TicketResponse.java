package com.grabtix.model.dto.response;

import com.grabtix.model.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class TicketResponse {
    private String id;
    private String eventId;
    private String eventName;
    private Ticket.Type type;
    private Double price;
}
