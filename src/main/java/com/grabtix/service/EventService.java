package com.grabtix.service;

import com.grabtix.model.dto.request.EventRequest;
import com.grabtix.model.dto.response.EventResponse;

import java.util.List;

public interface EventService {
    EventResponse addEvent(EventRequest eventRequest);
    EventResponse updateEvent(EventRequest eventRequest);
    EventResponse isDone(String id);
    List<EventResponse> viewMyEvent();
    List<EventResponse> viewAllEvent();
    List<EventResponse> findEventByName(String eventName);
    List<EventResponse> findEventByCategory(String category);
    List<EventResponse> viewUpcomingEvent();
    void deleteEvent(String id);
}
