package com.mf.jira.server.mapper;

import com.mf.jira.server.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> getAllUsers();

    int addUser (User user);

    User findUser(String name);
}
