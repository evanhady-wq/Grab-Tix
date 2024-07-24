package com.grabtix.service;

import com.grabtix.model.dto.request.CreatorRequest;
import com.grabtix.model.dto.response.CreatorResponse;

import java.util.List;

public interface CreatorService {
    CreatorResponse saveCreator(CreatorRequest creatorRequest);
    CreatorResponse viewMyProfile();
    CreatorResponse editMyProfile(CreatorRequest creatorRequest);
    void deleteAccount();
    CreatorResponse getById(String id);
    List<CreatorResponse> viewAllCreator();
    void deleteCreator(String id);
}
