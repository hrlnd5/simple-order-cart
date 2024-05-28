package com.example.restfulapi.service;

import com.example.restfulapi.entity.User;
import com.example.restfulapi.model.user.UpdateUserRequest;
import com.example.restfulapi.model.user.UserRequest;
import com.example.restfulapi.model.user.UserResponse;
import com.example.restfulapi.repository.UserRepository;
import com.example.restfulapi.security.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    private UserResponse toUserResponse(User user) {
        return UserResponse
                .builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }

    @Transactional
    public UserResponse register(UserRequest request) {
        validationService.validate(request);

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username already registered");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setName(request.getName());

        userRepository.save(user);

        return toUserResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse get(User user) {
        return toUserResponse(user);
    }

    @Transactional
    public UserResponse update(UpdateUserRequest request, User user) {
        validationService.validate(request);

        if (Objects.nonNull(request.getName())) {
            user.setName(request.getName());
        }

        if (Objects.nonNull(request.getPassword())) {
            user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        }

        userRepository.save(user);

        return toUserResponse(user);
    }
}
