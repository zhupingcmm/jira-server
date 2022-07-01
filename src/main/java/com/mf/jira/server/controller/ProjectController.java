package com.mf.jira.server.controller;

import com.mf.jira.server.base.BaseResponse;
import com.mf.jira.server.dto.ProjectDTO;
import com.mf.jira.server.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    @PostMapping("/project")
    public BaseResponse addProject(@RequestBody ProjectDTO projectDTO) {
        projectService.addProject(projectDTO);
        return BaseResponse.success();
    }
    @GetMapping("/projects")
    public BaseResponse<List<ProjectDTO>> getAllProjects(){
        return BaseResponse.success(projectService.getAllProjects());
    }
}
