package com.grabtix.controller;

import com.grabtix.constant.APIUrl;
import com.grabtix.model.dto.request.EventRequest;
import com.grabtix.model.dto.request.TransactionRequest;
import com.grabtix.model.dto.response.CommonResponse;
import com.grabtix.model.dto.response.CustomerResponse;
import com.grabtix.model.dto.response.EventResponse;
import com.grabtix.model.dto.response.TransactionResponse;
import com.grabtix.model.entity.Transaction;
import com.grabtix.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(APIUrl.TRANSACTION_API)
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping("/purchase")
    public ResponseEntity<CommonResponse<TransactionResponse>> purchaseTicket(@RequestBody TransactionRequest transactionRequest){
        TransactionResponse transactionResponse = transactionService.purchaseTicket(transactionRequest);
        CommonResponse<TransactionResponse> response = generateResponse("Purchasing Request Success, Please Paid to confirm!", Optional.of(transactionResponse));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PatchMapping("/cancel/{id}")
    public ResponseEntity<CommonResponse<TransactionResponse>> cancel(@PathVariable String id){
        transactionService.cancelTransaction(id);
        CommonResponse<TransactionResponse> response = generateResponse("Transaction Cancelled!", Optional.empty());
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PatchMapping("/payment/{id}")
    public ResponseEntity<CommonResponse<TransactionResponse>> successPaid(@PathVariable String id){
        TransactionResponse transactionResponse = transactionService.payment(id);
        CommonResponse<TransactionResponse> response = generateResponse("Transaction Success, Here is Your Ticket!", Optional.of(transactionResponse));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping
    public List<TransactionResponse> viewMyTransaction(){
        return transactionService.viewMyTransaction();
    }

    private CommonResponse<TransactionResponse> generateResponse(String message, Optional<TransactionResponse> transactionResponse) {
        return CommonResponse.<TransactionResponse>builder()
                .message(message)
                .data(transactionResponse)
                .build();
    }

}
