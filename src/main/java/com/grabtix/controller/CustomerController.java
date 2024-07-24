package com.grabtix.controller;


import com.grabtix.constant.APIUrl;
import com.grabtix.model.dto.request.CustomerRequest;
import com.grabtix.model.dto.response.CommonResponse;
import com.grabtix.model.dto.response.CustomerResponse;
import com.grabtix.model.dto.response.TicketResponse;
import com.grabtix.repository.TicketRepository;
import com.grabtix.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(APIUrl.CUSTOMER_API)
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    //CUSTOMER AUTHORITY
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/myProfile")
    public ResponseEntity<CommonResponse<CustomerResponse>> viewMyProfile(){
        CustomerResponse customerResponse = customerService.viewMyProfile();
        CommonResponse<CustomerResponse> response = generateCustomerResponse("Profile", Optional.of(customerResponse));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PutMapping("/myProfile/edit")
    public ResponseEntity<CommonResponse<CustomerResponse>> editMyProfile(@Valid @RequestBody CustomerRequest customerRequest) {
        CustomerResponse update = customerService.EditMyProfile(customerRequest);
        CommonResponse<CustomerResponse> response = generateCustomerResponse("Update Success", Optional.of(update));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @DeleteMapping("/myProfile/delete")
    public ResponseEntity<CommonResponse<CustomerResponse>> deleteAccount(){
        customerService.deleteAccount();
        CommonResponse<CustomerResponse> response = generateCustomerResponse("Delete Success", Optional.empty());
        return ResponseEntity.ok(response);
    }



    //ADMIN AUTHORITY
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<CustomerResponse> getAllCustomer(){
        return customerService.viewAllCustomer();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<CustomerResponse>> getCustomerById(@PathVariable String id) {
        CustomerResponse customerResponse = customerService.getById(id);
        CommonResponse<CustomerResponse> response = generateCustomerResponse("Search Result By ID", Optional.of(customerResponse));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<CustomerResponse>> deleteAccountCustomer(@PathVariable String id) {
        customerService.deleteCustomer(id);
        CommonResponse<CustomerResponse> response = generateCustomerResponse( "Delete Success", Optional.empty());
        return ResponseEntity.ok(response);
    }


    private CommonResponse<CustomerResponse> generateCustomerResponse(String message, Optional<CustomerResponse> customer) {
        return CommonResponse.<CustomerResponse>builder()
                .message(message)
                .data(customer)
                .build();
    }
}
