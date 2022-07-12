package com.mf.jira.server.controller;

import com.mf.jira.server.base.BaseResponse;
import com.mf.jira.server.dto.ProjectDTO;
import com.mf.jira.server.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public BaseResponse<List<ProjectDTO>> getAllProjects(@RequestParam(required = false) Long personId, @RequestParam(required = false) String name){
        return BaseResponse.success(projectService.getAllProjects(name, personId));
    }

    @PatchMapping("/project/{id}")
    public BaseResponse<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody ProjectDTO projectDTO){
        return BaseResponse.success(projectService.updateProject(id, projectDTO));
    }
}
