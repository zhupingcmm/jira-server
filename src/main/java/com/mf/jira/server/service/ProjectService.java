package com.mf.jira.server.service;

import com.mf.jira.server.dto.ProjectDTO;

import java.util.List;

public interface ProjectService {

    List<ProjectDTO> getAllProjects(String name, Long personId);

    void addProject(ProjectDTO projectDTO);

    ProjectDTO updateProject(Long id, ProjectDTO projectDTO);

    ProjectDTO getProjectById(Long id);

    void deleteProjectById(Long id);
}
