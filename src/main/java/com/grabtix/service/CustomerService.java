package com.grabtix.service;

import com.grabtix.model.dto.request.CustomerRequest;
import com.grabtix.model.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {
    CustomerResponse saveCustomer(CustomerRequest costumerRequest);
    CustomerResponse viewMyProfile();
    CustomerResponse EditMyProfile(CustomerRequest customerRequest);
    void deleteAccount();
    CustomerResponse getById(String id);
    List<CustomerResponse> viewAllCustomer();
    void deleteCustomer(String id);
}
