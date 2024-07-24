package com.grabtix.service.impl;

import com.grabtix.exceptions.AuthenticationException;
import com.grabtix.exceptions.ResourceNotFoundException;
import com.grabtix.model.dto.request.AuthRequest;
import com.grabtix.model.dto.request.CreatorRequest;
import com.grabtix.model.dto.request.CustomerRequest;
import com.grabtix.model.dto.response.LoginResponse;
import com.grabtix.model.dto.response.RegisterResponse;
import com.grabtix.model.entity.Role;
import com.grabtix.model.entity.User;
import com.grabtix.repository.UserRepository;
import com.grabtix.security.JwtUtil;
import com.grabtix.service.AuthService;
import com.grabtix.service.CreatorService;
import com.grabtix.service.CustomerService;
import com.grabtix.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final CustomerService customerService;
    private final CreatorService creatorService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;



    @Override
    public RegisterResponse registerCustomer(AuthRequest<CustomerRequest> authRequest) {
        Role role = roleService.getRoleOrSaveRole(Role.ERole.CUSTOMER);

        User user = User.builder()
                .email(authRequest.getEmail().toLowerCase())
                .password(passwordEncoder.encode(authRequest.getPassword()))
                .role(role)
                .build();

        user = userRepository.saveAndFlush(user);

        CustomerRequest requestData = authRequest.getData().orElseThrow(
                () -> new ResourceNotFoundException("Customer Not Found")
        );

        requestData.setUser(user);
        customerService.saveCustomer(requestData);

        return RegisterResponse.builder()
                .username(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public RegisterResponse registerCreator(AuthRequest<CreatorRequest> authRequest) {
        Role role = roleService.getRoleOrSaveRole(Role.ERole.CREATOR);

        User user = User.builder()
                .email(authRequest.getEmail().toLowerCase())
                .password(passwordEncoder.encode(authRequest.getPassword()))
                .role(role)
                .build();

        user = userRepository.saveAndFlush(user);

        CreatorRequest requestData = authRequest.getData().orElseThrow(
                () -> new ResourceNotFoundException("Creator Not Found")
        );

        requestData.setUser(user);
        creatorService.saveCreator(requestData);

        return RegisterResponse.builder()
                .username(user.getEmail())
                .role(user.getRole())
                .build();
    }


    @Override
    public RegisterResponse registerAdmin(AuthRequest authRequest) {
        Role role = roleService.getRoleOrSaveRole(Role.ERole.CREATOR);
        User user = User.builder()
                .email(authRequest.getEmail().toLowerCase())
                .password(passwordEncoder.encode(authRequest.getPassword()))
                .role(role)
                .build();

        user = userRepository.saveAndFlush(user);

        return RegisterResponse.builder()
                .username(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public LoginResponse login(AuthRequest<String> request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail().toLowerCase(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = (User) authentication.getPrincipal();

            String token = jwtUtil.generateToken(user);

            return LoginResponse.builder()
                    .token(token)
                    .role(user.getRole())
                    .build();
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Username or Password Invalid");
        }
    }
}
