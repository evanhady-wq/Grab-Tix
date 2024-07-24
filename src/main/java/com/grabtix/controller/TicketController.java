package com.grabtix.controller;

import com.grabtix.constant.APIUrl;
import com.grabtix.model.dto.response.CommonResponse;
import com.grabtix.model.dto.response.TicketResponse;
import com.grabtix.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(APIUrl.TICKET_API)
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping
    public List<TicketResponse> viewMyTicket() {
        return ticketService.viewMyTicket();

    }

    private CommonResponse<TicketResponse> generateResponse(String message, Optional<TicketResponse> ticketResponse) {
        return CommonResponse.<TicketResponse>builder()
                .message(message)
                .data(ticketResponse)
                .build();
    }

}
