package com.grabtix.service.impl;

import com.grabtix.model.entity.User;
import com.grabtix.repository.UserRepository;
import com.grabtix.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Invalid credential user"));

        return (UserDetails) user;
    }

    @Override
    public UserDetails loadUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("Invalid credential user"));

        return (UserDetails) user;
    }
}
