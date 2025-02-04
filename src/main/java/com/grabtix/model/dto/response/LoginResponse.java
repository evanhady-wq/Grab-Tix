package com.grabtix.model.dto.response;

import com.grabtix.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private Role role;

}
