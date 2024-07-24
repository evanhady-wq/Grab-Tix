package com.grabtix.service;


import com.grabtix.model.dto.request.AuthRequest;
import com.grabtix.model.dto.request.CreatorRequest;
import com.grabtix.model.dto.request.CustomerRequest;
import com.grabtix.model.dto.response.LoginResponse;
import com.grabtix.model.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse registerCustomer(AuthRequest<CustomerRequest> authRequest);
    RegisterResponse registerCreator(AuthRequest<CreatorRequest> authRequest);
    RegisterResponse registerAdmin(AuthRequest authRequest);
    LoginResponse login(AuthRequest<String> request);
}
