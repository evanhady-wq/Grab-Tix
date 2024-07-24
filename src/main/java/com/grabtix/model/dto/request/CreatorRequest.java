package com.grabtix.model.dto.request;

import com.grabtix.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatorRequest {
    private String id;
    private String name;
    private String description;
    private String phone;
    private User user;

}
