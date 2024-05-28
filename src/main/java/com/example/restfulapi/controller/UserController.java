package com.example.restfulapi.controller;

import com.example.restfulapi.entity.User;
import com.example.restfulapi.model.user.UpdateUserRequest;
import com.example.restfulapi.model.user.UserRequest;
import com.example.restfulapi.model.user.UserResponse;
import com.example.restfulapi.model.WebResponse;
import com.example.restfulapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> register(@RequestBody UserRequest request) {
        UserResponse response = userService.register(request);
        return WebResponse.<UserResponse>builder().data(response).build();
    }

    @GetMapping(
            path = "/current",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> get(User user) {
        UserResponse response = userService.get(user);
        return WebResponse.<UserResponse>builder().data(response).build();
    }

    @PatchMapping(
            path = "/current",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> update(User user, @RequestBody UpdateUserRequest request){
        UserResponse response = userService.update(request, user);
        return WebResponse.<UserResponse>builder().data(response).build();
    }
}
