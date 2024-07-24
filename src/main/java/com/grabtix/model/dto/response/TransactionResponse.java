package com.grabtix.model.dto.response;

import com.grabtix.model.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class TransactionResponse {
    private String id;
    private LocalDate transactionDate;
    private String customerId;
    private List<TicketResponse> ticketResponses;
    private Integer qty;
    private Double totalPrice;
    private Transaction.Status status;
    private LocalDate paidAt;
}
