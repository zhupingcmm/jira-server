package com.mf.jira.server.controller;

import com.mf.jira.server.base.BaseResponse;
import com.mf.jira.server.base.Constants;
import com.mf.jira.server.base.ResponseEnum;
import com.mf.jira.server.config.AppConfig;
import com.mf.jira.server.dto.UserDTO;
import com.mf.jira.server.service.UserService;
import com.mf.jira.server.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final RedisTemplate<String, String> redisTemplate;

    private final AppConfig appConfig;
    @GetMapping("/users")
    public BaseResponse<List<UserDTO>> getUsers () {
        List<UserDTO> users = userService.getUsers();
        log.info("Get all user info, {}", users);
        return BaseResponse.success(users);
    }

    @PostMapping("/register")
    public BaseResponse addUser(@RequestBody UserDTO userDTO){
        userService.addUser(userDTO);
        String token = JwtUtil.createToken(appConfig, userDTO.getToken(), userDTO.getPassword());

        updateToken(userDTO.getName(), token);

        UserDTO dto = UserDTO.builder()
                .name(userDTO.getName())
                .token(token)
                .build();
        return BaseResponse.success(dto);
    }

    private void updateToken(String key, String token) {
        //更新 redis 缓存
        redisTemplate.opsForValue().set(key, token, appConfig.getJwt().getTokenExpireTime(), TimeUnit.MILLISECONDS);
    }

    @PostMapping("/login")
    public BaseResponse login(@RequestBody UserDTO userDTO) {
        val result = userService.getUser(userDTO);
        String token = JwtUtil.createToken(appConfig, userDTO.getName(), userDTO.getPassword());
        result.setToken(token);
        updateToken(userDTO.getName(), token);
        return BaseResponse.success(result);
    }

    @GetMapping("/me")
    public BaseResponse getMeInfo(HttpServletRequest request){
       String token = request.getHeader("Authorization").replace("Bearer", "").trim();
       val claims = JwtUtil.parseClaims(token);
       String name = (String) claims.get(Constants.USERNAME);
       String password = (String) claims.get(Constants.PASSWORD);

       String tokenFromCache = redisTemplate.opsForValue().get(name);

       if (tokenFromCache == null) {
           return BaseResponse.error(ResponseEnum.TOKEN_EXPIRED);
       }
       val cacheClaims = JwtUtil.parseClaims(tokenFromCache);

       String cachePassword = (String) cacheClaims.get(Constants.PASSWORD);

       if (!Objects.equals(cachePassword, password)) {
           return BaseResponse.error(ResponseEnum.TOKEN_INVALID);
       }
       val result = userService.getUser(UserDTO.builder()
                       .name(name)
                       .password(password)
                       .token(token)
               .build());
       result.setPassword(null);
       return BaseResponse.success(result);
    }





}
