package com.grabtix.service.impl;

import com.grabtix.exceptions.ResourceNotFoundException;
import com.grabtix.model.dto.request.EventRequest;
import com.grabtix.model.dto.response.EventResponse;
import com.grabtix.model.entity.Creator;
import com.grabtix.model.entity.Event;
import com.grabtix.model.entity.User;
import com.grabtix.repository.CreatorRepository;
import com.grabtix.repository.EventRepository;
import com.grabtix.service.CreatorService;
import com.grabtix.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CreatorService creatorService;
    private final CreatorRepository creatorRepository;

    @Override
    public EventResponse addEvent(EventRequest eventRequest) {
        User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Creator creator = creatorRepository.findByUserId(loggedIn.getId());

        Event event = new Event();
        event.setName(eventRequest.getName());
        event.setDescription(eventRequest.getDescription());
        event.setCategory(eventRequest.getCategory());
        event.setLocation(eventRequest.getLocation());
        event.setDate(eventRequest.getDate());
        event.setVipQuota(eventRequest.getVipQuota());
        event.setRegularQuota(eventRequest.getRegularQuota());
        event.setVipTicketPrice(eventRequest.getVipTicketPrice());
        event.setRegularTicketPrice(eventRequest.getRegularTicketPrice());
        event.setEventCreator(creator);

        eventRepository.saveAndFlush(event);
        return convertToResponse(event);
    }

    @Override
    public EventResponse updateEvent(EventRequest eventRequest) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Creator creator = creatorRepository.findByUserId(loggedInUser.getId());

        List<Event> eventList = eventRepository.findByCreatorId(creator.getId());

        Event updatedEvent = null;
        for (Event event : eventList) {
            if (event.getId().equals(eventRequest.getId())) {
                event.setName(eventRequest.getName());
                event.setCategory(eventRequest.getCategory());
                event.setDescription(eventRequest.getDescription());
                event.setLocation(eventRequest.getLocation());
                event.setDate(eventRequest.getDate());
                event.setVipQuota(eventRequest.getVipQuota());
                event.setRegularQuota(eventRequest.getRegularQuota());
                event.setVipTicketPrice(eventRequest.getVipTicketPrice());
                event.setRegularTicketPrice(eventRequest.getRegularTicketPrice());
                updatedEvent = event;
                break;
            }
        }

        if (updatedEvent == null) {
            throw new ResourceNotFoundException("Event not found for update");
        }

        eventRepository.saveAndFlush(updatedEvent);
        return convertToResponse(updatedEvent);
    }

    @Override
    public EventResponse isDone(String id) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Creator creator = creatorRepository.findByUserId(loggedInUser.getId());

        List<Event> eventList = eventRepository.findByCreatorId(creator.getId());

        Event updatedEvent = null;
        for (Event event : eventList) {
            if (event.getId().equals(id)) {
                event.setIsDone(true);
                updatedEvent = event;
                break;
            }
        }

        if (updatedEvent == null) {
            throw new ResourceNotFoundException("Event not found for update");
        }
        eventRepository.saveAndFlush(updatedEvent);
        return convertToResponse(updatedEvent);
    }

    @Override
    public List<EventResponse> viewMyEvent() {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Creator creator = creatorRepository.findByUserId(loggedInUser.getId());

        return eventRepository.findByCreatorId(creator.getId()).stream().map(this::convertToResponse)
                .collect(Collectors.toList());

    }

    @Override
    public List<EventResponse> viewAllEvent() {
        return eventRepository.findAll().stream().map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventResponse> findEventByName(String eventName) {
        return eventRepository.findAllByNameContaining(eventName).stream().map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventResponse> findEventByCategory(String category) {
        return eventRepository.findByCategory(category).stream().map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventResponse> viewUpcomingEvent() {
        return eventRepository.findUpcomingEvents().stream().map(this::convertToResponse)
                .toList();
    }

    @Override
    public void deleteEvent(String id) {
        Event event = findEventById(id);
        if(event.getIsDone().equals(true)){
            eventRepository.delete(event);
        } else throw new ResourceNotFoundException("Event still Active");
    }

    private Event findEventById(String id){
        return eventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer Not Found"));
    }

    private EventResponse convertToResponse(Event event){
        return EventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .category(event.getCategory())
                .location(event.getLocation())
                .date(event.getDate())
                .vipQuota(event.getVipQuota())
                .vipTicketPrice(event.getVipTicketPrice())
                .regularQuota(event.getRegularQuota())
                .regularTicketPrice(event.getRegularTicketPrice())
                .creatorName(event.getEventCreator().getName())
                .build();
    }
}
