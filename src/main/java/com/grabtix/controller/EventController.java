package com.grabtix.controller;

import com.grabtix.constant.APIUrl;
import com.grabtix.model.dto.request.CreatorRequest;
import com.grabtix.model.dto.request.EventRequest;
import com.grabtix.model.dto.response.CommonResponse;
import com.grabtix.model.dto.response.CreatorResponse;
import com.grabtix.model.dto.response.CustomerResponse;
import com.grabtix.model.dto.response.EventResponse;
import com.grabtix.model.entity.Event;
import com.grabtix.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.batch.BatchTransactionManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(APIUrl.EVENT_API)
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;


    @PreAuthorize("hasRole('ROLE_CREATOR')")
    @PostMapping("/addEvent")
    public ResponseEntity<CommonResponse<EventResponse>> addEvent(@RequestBody EventRequest eventRequest){
        EventResponse eventResponse = eventService.addEvent(eventRequest);
        CommonResponse<EventResponse> response = generateEventResponse("Add Event Success", Optional.of(eventResponse));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_CREATOR')")
    @PutMapping("/myEvent/edit")
    public ResponseEntity<CommonResponse<EventResponse>> updateEvent(@Valid @RequestBody EventRequest eventRequest) {
        EventResponse update = eventService.updateEvent(eventRequest);
        CommonResponse<EventResponse> response = generateEventResponse("Edit Event Success", Optional.of(update));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_CREATOR')")
    @GetMapping("/myEvent")
    public List<EventResponse> viewMyEvent() {
        return eventService.viewMyEvent();
    }

    @PreAuthorize("hasRole('ROLE_CREATOR')")
    @PatchMapping("/myEvent/{id}/update")
    public ResponseEntity<CommonResponse<EventResponse>> updateStatus(@PathVariable String id){
        EventResponse eventStatus = eventService.isDone(id);
        CommonResponse<EventResponse> response = generateEventResponse("Event is Done", Optional.of(eventStatus));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping
    List<EventResponse> viewAllEvent(){
        return eventService.viewAllEvent();
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/search/name/")
    List<EventResponse> searchEventByName(@RequestParam String name){
        return eventService.findEventByName(name);
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/search/category/")
    List<EventResponse> searchEventByCategory(@RequestParam String category){
        return eventService.findEventByCategory(category);
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER)")
    @GetMapping("/upcoming")
    List<EventResponse> upcomingEvent(){
        return eventService.viewUpcomingEvent();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/delete/{id}")
    public ResponseEntity<CommonResponse<EventResponse>> deleteEventByAdmin(@PathVariable String id) {
        eventService.deleteEvent(id);
        CommonResponse<EventResponse> response = generateEventResponse("Delete Success", Optional.empty());
        return ResponseEntity.ok(response);
    }

    private CommonResponse<EventResponse> generateEventResponse (String message, Optional<EventResponse> eventResponse) {
        return CommonResponse.<EventResponse>builder()
                .message(message)
                .data(eventResponse)
                .build();
    }

}
