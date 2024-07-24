package com.grabtix.service.impl;

import com.grabtix.exceptions.ResourceNotFoundException;
import com.grabtix.model.dto.request.CreatorRequest;
import com.grabtix.model.dto.response.CreatorResponse;
import com.grabtix.model.dto.response.CustomerResponse;
import com.grabtix.model.entity.Creator;
import com.grabtix.model.entity.Customer;
import com.grabtix.model.entity.User;
import com.grabtix.repository.CreatorRepository;
import com.grabtix.service.CreatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreatorServiceImpl implements CreatorService{
    private final CreatorRepository creatorRepository;

    //CREATOR AUTHORITY
    @Override
    public CreatorResponse saveCreator(CreatorRequest creatorRequest) {
        Creator creator = Creator.builder()
                .name(creatorRequest.getName())
                .description(creatorRequest.getDescription())
                .phone(creatorRequest.getPhone())
                .user(creatorRequest.getUser())
                .build();

        creatorRepository.saveAndFlush(creator);
        return convertToResponse(creator);
    }

    @Override
    public CreatorResponse editMyProfile(CreatorRequest creatorRequest) {
        User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Creator creator = creatorRepository.findByUserId(loggedIn.getId());

        creator.setName(creatorRequest.getName());
        creator.setDescription(creatorRequest.getDescription());
        creator.setPhone(creator.getPhone());

        creatorRepository.saveAndFlush(creator);
        return convertToResponse(creator);
    }

    @Override
    //SoftDeleted
    public void deleteAccount() {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Creator creator = creatorRepository.findByUserId(loggedInUser.getId());
        creator.setIsDeleted(true);

        creatorRepository.saveAndFlush(creator);
    }

    @Override
    public CreatorResponse viewMyProfile() {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Creator creator = creatorRepository.findByUserId(loggedInUser.getId());
        return convertToResponse(creator) ;
    }

    //ADMIN AUTHORITY

    @Override
    public CreatorResponse getById(String id) {
        return convertToResponse(findCreatorById(id));
    }

    @Override
    public List<CreatorResponse> viewAllCreator() {
        return creatorRepository.findAll().stream().map(this::convertToResponse).toList();
    }


    @Override
    public void deleteCreator(String id) {
        Creator creator = findCreatorById(id);
        if(creator.getIsDeleted().equals(true)){
            creatorRepository.delete(creator);
        } else throw new ResourceNotFoundException("Customer still Active");
    }

    public Creator findCreatorById(String id) {
        return  creatorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Creator Not Found"));
    }

    private CreatorResponse convertToResponse(Creator creator) {
        return CreatorResponse.builder()
                .id(creator.getId())
                .name(creator.getName())
                .description(creator.getDescription())
                .phone(creator.getPhone())
                .build();
    }
}
