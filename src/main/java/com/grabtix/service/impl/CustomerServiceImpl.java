package com.grabtix.service.impl;

import com.grabtix.exceptions.ResourceNotFoundException;
import com.grabtix.model.dto.request.CustomerRequest;
import com.grabtix.model.dto.response.CustomerResponse;
import com.grabtix.model.entity.Customer;
import com.grabtix.model.entity.User;
import com.grabtix.repository.CustomerRepository;
import com.grabtix.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    //CREATOR AUTHORITY

    @Override
    public CustomerResponse saveCustomer(CustomerRequest customerRequest) {
        Customer customer = new Customer();
        customer.setName(customerRequest.getName());
        customer.setPhone(customerRequest.getPhone());
        customer.setUser(customerRequest.getUser());

        customerRepository.saveAndFlush(customer);
        return convertToResponse(customer);
    }

    @Override
    public CustomerResponse EditMyProfile(CustomerRequest customerRequest) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepository.findByUserId(loggedInUser.getId());

        customer.setName(customerRequest.getName());
        customer.setPhone(customerRequest.getPhone());

        customerRepository.saveAndFlush(customer);
        return convertToResponse(customer);
    }

    @Override
    //SoftDeleted
    public void deleteAccount() {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepository.findByUserId(loggedInUser.getId());
        customer.setIsDeleted(true);

        customerRepository.saveAndFlush(customer);
    }

    @Override
    public CustomerResponse viewMyProfile() {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepository.findByUserId(loggedInUser.getId());
        return convertToResponse(customer) ;
    }

    //ADMIN AUTHORITY
    @Override
    public CustomerResponse getById(String id) {
        return convertToResponse(findCustomerById(id));
    }

    @Override
    public List<CustomerResponse> viewAllCustomer() {
        return customerRepository.findAll().stream().map(this::convertToResponse).toList();
    }

    @Override
    public void deleteCustomer(String id) {
        Customer customer = findCustomerById(id);
        if(customer.getIsDeleted().equals(true)){
            customerRepository.delete(findCustomerById(id));
        } else throw new ResourceNotFoundException("Customer still Active");
    }

    private Customer findCustomerById(String id){
        return customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer Not Found"));
    }

    private CustomerResponse convertToResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .phone(customer.getPhone())
                .build();
    }
}
