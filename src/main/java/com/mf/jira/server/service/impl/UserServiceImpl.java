package com.mf.jira.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.mf.jira.server.base.ResponseEnum;
import com.mf.jira.server.dto.UserDTO;
import com.mf.jira.server.exception.JiraException;
import com.mf.jira.server.mapper.UserMapper;
import com.mf.jira.server.model.User;
import com.mf.jira.server.service.UserService;
import com.mf.jira.server.util.Assert;
import com.mf.jira.server.util.ObjectTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Override
    public void addUser(UserDTO userDTO) {

        User user = ObjectTransformer.transform(userDTO, User.class);
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send("order.usd2cny", JSON.toJSONString(user));
        try {
            String res = future.get().toString();
            log.info("producer send result: [{}]", res);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        int result = userMapper.addUser(user);
        Assert.singleRowAffected(result);
    }

    @Override
    @Cacheable(cacheNames = "users", key = "")
    public List<UserDTO> getUsers() {
        List<User> users = userMapper.getAllUsers();
        return ObjectTransformer.transform(users, UserDTO.class);
    }

    @Override
    public UserDTO getUser(UserDTO userDTO) {
        User user = userMapper.findUser(userDTO.getName());
        if (Objects.equals(user.getPassword(), userDTO.getPassword())) {
            return ObjectTransformer.transform(user,UserDTO.class);
        }
        throw new JiraException(ResponseEnum.NAME_OR_PASSWORD_NOT_VALID);
    }
}
