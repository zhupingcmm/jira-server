package com.mf.jira.server.controller;

import com.mf.jira.server.base.BaseResponse;
import com.mf.jira.server.dto.UserDTO;
import com.mf.jira.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/users")
    public BaseResponse<List<UserDTO>> getUsers () {
        return BaseResponse.success(userService.getUsers());
    }

    @PostMapping("/user")
    public BaseResponse addUser(@RequestBody UserDTO userDTO){
        userService.addUser(userDTO);
        return BaseResponse.success();
    }
}
