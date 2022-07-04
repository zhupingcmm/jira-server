package com.mf.jira.server.service;

import com.mf.jira.server.dto.UserDTO;

import java.util.List;

public interface UserService {
    void addUser(UserDTO userDTO);

    List<UserDTO> getUsers();

    UserDTO getUser(UserDTO userDTO);
}
