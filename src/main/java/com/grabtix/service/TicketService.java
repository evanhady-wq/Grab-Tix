package com.grabtix.service;

import com.grabtix.model.dto.request.TicketRequest;
import com.grabtix.model.dto.response.TicketResponse;
import com.grabtix.model.entity.Ticket;

import java.util.List;

public interface TicketService {
    Ticket reserveTicket(TicketRequest ticketRequest);
    List<TicketResponse> viewMyTicket();
}
