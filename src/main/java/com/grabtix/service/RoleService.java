package com.grabtix.service;


import com.grabtix.model.entity.Role;

public interface RoleService {
    Role getRoleOrSaveRole(Role.ERole role);

}
