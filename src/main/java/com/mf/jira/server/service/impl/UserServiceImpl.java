package com.mf.jira.server.service.impl;

import com.mf.jira.server.base.ResponseEnum;
import com.mf.jira.server.dto.UserDTO;
import com.mf.jira.server.exception.JiraException;
import com.mf.jira.server.mapper.UserMapper;
import com.mf.jira.server.model.User;
import com.mf.jira.server.service.UserService;
import com.mf.jira.server.util.Assert;
import com.mf.jira.server.util.ObjectTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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

    @Override
    public UserDTO getUser(UserDTO userDTO) {
        User user = userMapper.findUser(userDTO.getName());
        if (Objects.equals(user.getPassword(), userDTO.getPassword())) {
            return ObjectTransformer.transform(user,UserDTO.class);
        };
        throw new JiraException(ResponseEnum.NAME_OR_PASSWORD_NOT_VALID);
    }
}
