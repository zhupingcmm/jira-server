package com.mf.jira.server.controller;

import com.mf.jira.server.base.BaseResponse;
import com.mf.jira.server.base.ResponseEnum;
import com.mf.jira.server.config.AppConfig;
import com.mf.jira.server.dto.UserDTO;
import com.mf.jira.server.service.UserService;
import com.mf.jira.server.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final AppConfig appConfig;
    @GetMapping("/users")
    public BaseResponse<List<UserDTO>> getUsers () {
        return BaseResponse.success(userService.getUsers());
    }

    @PostMapping("/register")
    public BaseResponse addUser(@RequestBody UserDTO userDTO){
        userService.addUser(userDTO);
        String token = JwtUtil.createToken(appConfig, userDTO.getToken(), userDTO.getPassword());
        UserDTO dto = UserDTO.builder()
                .name(userDTO.getName())
                .token(token)
                .build();
        return BaseResponse.success(dto);
    }

    @PostMapping("/login")
    public BaseResponse login(@RequestBody UserDTO userDTO) {
        val result = userService.getUser(userDTO);
        String token = JwtUtil.createToken(appConfig, userDTO.getName(), userDTO.getPassword());
        result.setToken(token);
        return BaseResponse.success(result);
    }

    @GetMapping("/me")
    public BaseResponse<UserDTO> getMeInfo(HttpServletRequest request){
       String token = request.getHeader("Authorization").replace("Bearer", "").trim();
       val claims = JwtUtil.parseClaims(token);
       String name = (String) claims.get("username");
       String password = (String) claims.get("password");

       val result = userService.getUser(UserDTO.builder()
                       .name(name)
                       .token(token)
               .build());
       return BaseResponse.success(result);
    }


}
