package com.mf.jira.server.service.impl;

import com.mf.jira.server.dto.ProjectDTO;
import com.mf.jira.server.mapper.ProjectMapper;
import com.mf.jira.server.model.Project;
import com.mf.jira.server.service.ProjectService;
import com.mf.jira.server.util.Assert;
import com.mf.jira.server.util.ObjectTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectMapper projectMapper;
    @Override
    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectMapper.getAllProjects();
        return ObjectTransformer.transform(projects, ProjectDTO.class);
    }

    @Override
    public void addProject(ProjectDTO projectDTO) {
       int result = projectMapper.addProject(ObjectTransformer.transform(projectDTO, Project.class));
        Assert.singleRowAffected(result);
    }
}
