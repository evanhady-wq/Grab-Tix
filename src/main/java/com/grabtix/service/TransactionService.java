package com.grabtix.service;

import com.grabtix.model.dto.request.TransactionRequest;
import com.grabtix.model.dto.response.TransactionResponse;

import java.util.List;

public interface TransactionService {
    TransactionResponse purchaseTicket(TransactionRequest transactionRequest);
    void cancelTransaction(String id);
    TransactionResponse payment(String id);
    List<TransactionResponse> viewMyTransaction();

}
