package com.grabtix.controller;


import com.grabtix.constant.APIUrl;
import com.grabtix.model.dto.request.AuthRequest;
import com.grabtix.model.dto.request.CreatorRequest;
import com.grabtix.model.dto.request.CustomerRequest;
import com.grabtix.model.dto.response.CommonResponse;
import com.grabtix.model.dto.response.LoginResponse;
import com.grabtix.model.dto.response.RegisterResponse;
import com.grabtix.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(APIUrl.AUTH_API)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register/customer")
    public ResponseEntity<CommonResponse<RegisterResponse>> registerCustomer(@RequestBody AuthRequest<CustomerRequest> authRequest) {
        RegisterResponse registered = authService.registerCustomer(authRequest);
        CommonResponse<RegisterResponse> response = generateRegisterResponse(Optional.of(registered));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/register/creator")
    public ResponseEntity<CommonResponse<RegisterResponse>> registerSeller(@RequestBody AuthRequest<CreatorRequest> authRequest) {
        RegisterResponse registered = authService.registerCreator(authRequest);
        CommonResponse<RegisterResponse> response = generateRegisterResponse(Optional.of(registered));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<CommonResponse<RegisterResponse>> registerAdmin(@RequestBody AuthRequest authRequest) {
        RegisterResponse registered = authService.registerAdmin(authRequest);
        CommonResponse<RegisterResponse> response = generateRegisterResponse(Optional.of(registered));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResponse>> login(@RequestBody AuthRequest<String> authRequest) {
        LoginResponse login = authService.login(authRequest);

        CommonResponse<LoginResponse> response = CommonResponse.<LoginResponse>builder()
                .message("Login Success")
                .data(Optional.of(login))
                .build();

        return ResponseEntity.ok(response);

    }

    private CommonResponse<RegisterResponse> generateRegisterResponse(Optional<RegisterResponse> registerResponse) {
        return CommonResponse.<RegisterResponse>builder()
                .message("Account Successfully Registered!")
                .data(registerResponse)
                .build();
    }

}
