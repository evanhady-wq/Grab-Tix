package com.grabtix.service.impl;

import com.grabtix.exceptions.ResourceNotFoundException;
import com.grabtix.model.dto.request.TicketRequest;
import com.grabtix.model.dto.response.TicketResponse;
import com.grabtix.model.entity.Customer;
import com.grabtix.model.entity.Event;
import com.grabtix.model.entity.Ticket;
import com.grabtix.model.entity.User;
import com.grabtix.repository.CustomerRepository;
import com.grabtix.repository.EventRepository;
import com.grabtix.repository.TicketRepository;
import com.grabtix.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final CustomerRepository customerRepository;

    @Override
    public Ticket reserveTicket(TicketRequest ticketRequest) {
        User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepository.findByUserId(loggedIn.getId());

        Optional<Event> eventOptional = eventRepository.findById(ticketRequest.getEventId());
        if (eventOptional.isEmpty()) {
            throw new ResourceNotFoundException("Event not found");
        }

        Event event = eventOptional.get();

        if (!event.hasAvailableTickets(ticketRequest.getTicketType(), 1)) {
            throw new RuntimeException("No available tickets for the selected category");
        }

        Double price = ticketRequest.getTicketType() == Ticket.Type.VIP ? event.getVipTicketPrice() : event.getRegularTicketPrice();

        Ticket ticket = Ticket.builder()
                .event(event)
                .ticketType(ticketRequest.getTicketType())
                .price(price)
                .build();

        ticketRepository.save(ticket);
        event.reduceQuota(ticket.getTicketType(), 1);
        eventRepository.save(event);

        return ticket;
    }

    @Override
    public List<TicketResponse> viewMyTicket() {
        User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepository.findByUserId(loggedIn.getId());

        return ticketRepository.findPaidTicket(customer.getId()).stream().map(this::convertToResponse)
                .toList();

    }


    private TicketResponse convertToResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .eventId(ticket.getEvent().getId())
                .eventName(ticket.getEvent().getName())
                .type(ticket.getTicketType())
                .price(ticket.getPrice())
                .build();
    }
}
