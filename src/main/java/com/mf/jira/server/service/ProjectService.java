package com.mf.jira.server.service;

import com.mf.jira.server.dto.ProjectDTO;

import java.util.List;

public interface ProjectService {

    List<ProjectDTO> getAllProjects(String name, Long personId);

    void addProject(ProjectDTO projectDTO);
}
