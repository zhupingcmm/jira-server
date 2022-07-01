package com.mf.jira.server.service;

import com.mf.jira.server.dto.ProjectDTO;

import java.util.List;

public interface ProjectService {

    List<ProjectDTO> getAllProjects();

    void addProject(ProjectDTO projectDTO);
}
