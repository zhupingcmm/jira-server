package com.mf.jira.server.mapper;

import com.mf.jira.server.model.Project;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProjectMapper {
    int addProject(Project project);

    List<Project> getAllProjects();

    List<Project> getProject(Project project);

    Project getProjectById(Long id);

    int updateProjectPin(Project project);

    void deleteProjectById(Long id);
}
