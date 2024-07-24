package com.grabtix.controller;


import com.grabtix.constant.APIUrl;
import com.grabtix.model.dto.request.CreatorRequest;
import com.grabtix.model.dto.response.CommonResponse;
import com.grabtix.model.dto.response.CreatorResponse;
import com.grabtix.service.CreatorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(APIUrl.CREATOR_API)
@RequiredArgsConstructor
public class CreatorController {
    private final CreatorService creatorService;

    //CREATOR AUTHORITY
    @PreAuthorize("hasRole('ROLE_CREATOR')")
    @GetMapping("/myProfile")
    public ResponseEntity<CommonResponse<CreatorResponse>> viewMyProfile(){
        CreatorResponse creatorResponse = creatorService.viewMyProfile();
        CommonResponse<CreatorResponse> response = generateCreatorResponse("Profile", Optional.of(creatorResponse));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_CREATOR')")
    @PutMapping("/myProfile/edit")
    public ResponseEntity<CommonResponse<CreatorResponse>> editMyProfile(@Valid @RequestBody CreatorRequest creatorRequest) {
        CreatorResponse update = creatorService.editMyProfile(creatorRequest);
        CommonResponse<CreatorResponse> response = generateCreatorResponse("Update Success", Optional.of(update));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_CREATOR')")
    @DeleteMapping("/myProfile/delete")
    public ResponseEntity<CommonResponse<CreatorResponse>> deleteAccount(){
        creatorService.deleteAccount();
        CommonResponse<CreatorResponse> response = generateCreatorResponse("Delete Success", Optional.empty());
        return ResponseEntity.ok(response);
    }

    //ADMIN AUTHORITY
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<CreatorResponse> getAllCreator(){
        return creatorService.viewAllCreator();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<CreatorResponse>> getCreatorById(@PathVariable String id) {
        CreatorResponse creatorResponse = creatorService.getById(id);
        CommonResponse<CreatorResponse> response = generateCreatorResponse("Search Result By ID", Optional.of(creatorResponse));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommonResponse<CreatorResponse>> deleteAccountCreator(@PathVariable String id) {
        creatorService.deleteCreator(id);
        CommonResponse<CreatorResponse> response = generateCreatorResponse("Delete Success", Optional.empty());
        return ResponseEntity.ok(response);
    }


    private CommonResponse<CreatorResponse> generateCreatorResponse(String message, Optional<CreatorResponse> creator) {
        return CommonResponse.<CreatorResponse>builder()
                .message(message)
                .data(creator)
                .build();
    }
}

