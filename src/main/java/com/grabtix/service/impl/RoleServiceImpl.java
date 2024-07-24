package com.grabtix.service.impl;


import com.grabtix.model.entity.Role;
import com.grabtix.repository.RoleRepository;
import com.grabtix.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getRoleOrSaveRole(Role.ERole role) {
        Optional<Role> theRoles = roleRepository.findByName(role);
        if(theRoles.isPresent()){
            return theRoles.get();
        }
        Role currentRole = Role.builder()
                .name(role)
                .build();
        return roleRepository.saveAndFlush(currentRole);
    }
}
