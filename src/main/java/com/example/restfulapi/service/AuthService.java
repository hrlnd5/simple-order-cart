package com.example.restfulapi.service;

import com.example.restfulapi.entity.User;
import com.example.restfulapi.model.user.LoginUserRequest;
import com.example.restfulapi.model.user.TokenResponse;
import com.example.restfulapi.repository.UserRepository;
import com.example.restfulapi.security.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    private Long nextNDaysInMillis(Long days) {
        return System.currentTimeMillis() + (1000 * 60 * 60 * 24 * days);
    }

    @Transactional
    public TokenResponse login(LoginUserRequest request) {
        validationService.validate(request);

        User user = userRepository
                .findFirstByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is wrong"));

        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(nextNDaysInMillis(30L));
            userRepository.save(user);

            return TokenResponse
                    .builder()
                    .token(user.getToken())
                    .tokenExpiredAt(user.getTokenExpiredAt())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is wrong");
        }
    }

    @Transactional
    public void logout(User user) {
        user.setToken(null);
        user.setTokenExpiredAt(null);

        userRepository.save(user);
    }

}
