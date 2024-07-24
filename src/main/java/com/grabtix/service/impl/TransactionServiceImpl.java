package com.grabtix.service.impl;

import com.grabtix.exceptions.ResourceNotFoundException;
import com.grabtix.model.dto.request.TransactionRequest;
import com.grabtix.model.dto.response.TicketResponse;
import com.grabtix.model.dto.response.TransactionResponse;
import com.grabtix.model.entity.Customer;
import com.grabtix.model.entity.Ticket;
import com.grabtix.model.entity.Transaction;
import com.grabtix.model.entity.User;
import com.grabtix.repository.CustomerRepository;
import com.grabtix.repository.TicketRepository;
import com.grabtix.repository.TransactionRepository;
import com.grabtix.service.TicketService;
import com.grabtix.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TicketRepository ticketRepository;
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final TicketService ticketService;

    @Override
    @Transactional
    public TransactionResponse purchaseTicket(TransactionRequest transactionRequest) {
        User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepository.findByUserId(loggedIn.getId());

        List<Ticket> ticketList = new ArrayList<>();
        double totalPrice = 0;
        for (int i = 0; i < transactionRequest.getQty(); i++) {
            Ticket ticket = ticketService.reserveTicket(transactionRequest.getTicketRequests().get(0));
            ticketList.add(ticket);
            totalPrice += ticket.getPrice();
        }


        Transaction transaction = Transaction.builder()
                .customer(customer)
                .transactionDate(LocalDate.now())
                .ticket(ticketList)
                .qty(transactionRequest.getQty())
                .totalPrice(totalPrice)
                .status(Transaction.Status.UNPAID)
                .build();

        ticketList.forEach(ticket -> ticket.setTransaction(transaction));

        transactionRepository.save(transaction);
        return convertToTransactionResponse(transaction);
    }

    @Override
    public void cancelTransaction(String id) {
        User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepository.findByUserId(loggedIn.getId());

        List<Transaction> transactions = transactionRepository.findTransactionByCustomerId(customer.getId());

        Transaction cancelTransaction = null;
        for (Transaction transaction : transactions) {
            if (transaction.getId().equals(id)) {
                transaction.setStatus(Transaction.Status.CANCELLED);
                cancelTransaction = transaction;
                break;
            }
        }

        if (cancelTransaction == null) {
            throw new ResourceNotFoundException("Event not found for cancel");
        }
        transactionRepository.saveAndFlush(cancelTransaction);
    }

    @Override
    public TransactionResponse payment(String id) {
        User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepository.findByUserId(loggedIn.getId());

        List<Transaction> transactions = transactionRepository.findTransactionByCustomerId(customer.getId());

        Transaction haveBeenPaid = null;
        for (Transaction transaction : transactions) {
            if (transaction.getId().equals(id)) {
                transaction.setStatus(Transaction.Status.CANCELLED);
                haveBeenPaid = transaction;
                break;
            }
        }

        if (haveBeenPaid == null) {
            throw new ResourceNotFoundException("Event not found for paid");
        }
        transactionRepository.saveAndFlush(haveBeenPaid);

        return convertToTransactionResponse(haveBeenPaid);
    }

    private TicketResponse convertToTicketResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .customerId(ticket.getCustomer().getId())
                .eventId(ticket.getEvent().getId())
                .eventName(ticket.getEvent().getName())
                .type(ticket.getTicketType())
                .price(ticket.getPrice())
                .build();
    }

    private List<TicketResponse> convertTicketResponseList (List<Ticket> ticketList){
        return ticketList.stream()
                .map(this::convertToTicketResponse)
                .collect(Collectors.toList());
    }

    private TransactionResponse convertToTransactionResponse(Transaction transaction){
        return TransactionResponse.builder()
                .id(transaction.getId())
                .customerId(transaction.getCustomer().getId())
                .transactionDate(transaction.getTransactionDate())
                .ticketResponses(convertTicketResponseList(transaction.getTicket()))
                .qty(transaction.getQty())
                .totalPrice(transaction.getTotalPrice())
                .status(transaction.getStatus())
                .paidAt(transaction.getPaidAt())
                .build();
    }

}
