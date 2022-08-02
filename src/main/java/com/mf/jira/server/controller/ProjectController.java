package com.mf.jira.server.controller;

import com.mf.jira.server.base.BaseResponse;
import com.mf.jira.server.base.ResponseEnum;
import com.mf.jira.server.dto.ProjectDTO;
import com.mf.jira.server.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
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
        log.info("update project  info {}", projectDTO);
        return BaseResponse.success(projectService.updateProject(id, projectDTO), ResponseEnum.PROJECT_MODIFY_SUCCESS);
    }

    @GetMapping("/project/{id}")
    public BaseResponse<ProjectDTO> getProject(@PathVariable Long id){

        ProjectDTO projectDTO = projectService.getProjectById(id);
        log.info("get project info {}", projectDTO);
        return BaseResponse.success(projectDTO);
    }

    @DeleteMapping("/project/{id}")
    public BaseResponse deleteProjectById(@PathVariable Long id) {
        log.info("delete project {}", id);
        projectService.deleteProjectById(id);
        return BaseResponse.success();
    }
}
