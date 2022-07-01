package com.mf.jira.server.service.impl;

import com.mf.jira.server.dto.UserDTO;
import com.mf.jira.server.mapper.UserMapper;
import com.mf.jira.server.model.User;
import com.mf.jira.server.service.UserService;
import com.mf.jira.server.util.Assert;
import com.mf.jira.server.util.ObjectTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    @Override
    public void addUser(UserDTO userDTO) {
        User user = ObjectTransformer.transform(userDTO, User.class);
        int result = userMapper.addUser(user);
        Assert.singleRowAffected(result);
    }

    @Override
    public List<UserDTO> getUsers() {
        List<User> users = userMapper.getAllUsers();
        return ObjectTransformer.transform(users, UserDTO.class);
    }
}
