package com.grabtix.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {
    private String id;
    private LocalDate transactionDate;
    private String customerId;
    private List<TicketRequest> ticketRequests;
    private Integer qty;
    private Double totalPrice;
}
