package com.mf.jira.server.service.impl;

import com.mf.jira.server.dto.ProjectDTO;
import com.mf.jira.server.exception.JiraException;
import com.mf.jira.server.mapper.ProjectMapper;
import com.mf.jira.server.model.Project;
import com.mf.jira.server.service.ProjectService;
import com.mf.jira.server.util.Assert;
import com.mf.jira.server.util.ObjectTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectMapper projectMapper;
    @Override
    @Transactional(rollbackFor = JiraException.class)
    public List<ProjectDTO> getAllProjects(String name, Long personId) {
        List<Project> projects = projectMapper.getAllProjects();
        if (name != null || personId != null) {
            if (name != null) {
                projects = projects.stream()
                        .filter(project -> project.getName().equals(name))
                        .collect(Collectors.toList());
            }
            if (personId != null) {
                projects = projects.stream()
                        .filter(project -> project.getPersonId().equals(personId))
                        .collect(Collectors.toList());
            }
        }

        return ObjectTransformer.transform(projects, ProjectDTO.class);
    }

    @Override
    public void addProject(ProjectDTO projectDTO) {
       int result = projectMapper.addProject(ObjectTransformer.transform(projectDTO, Project.class));
        Assert.singleRowAffected(result);
    }

    @Override
    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO) {
        Project project = projectMapper.getProjectById(id);
        if (projectDTO.getPin() != null) {
            project.setPin(projectDTO.getPin());
        }
        int result = projectMapper.updateProjectPin(project);
        Assert.singleRowAffected(result);
        return ObjectTransformer.transform(project, ProjectDTO.class);
    }
}
