package io.hellcoding.psa.api;

import static io.hellcoding.psa.api.dto.DtoMapper.toBizOpty;
import static io.hellcoding.psa.api.dto.DtoMapper.toProject;
import static io.hellcoding.psa.api.dto.DtoMapper.toProjectMember;
import static io.hellcoding.psa.api.dto.DtoMapper.toProjectTask;

import io.hellcoding.psa.api.dto.Dtos.AddProjectMemberRequest;
import io.hellcoding.psa.api.dto.Dtos.BizOptyResponse;
import io.hellcoding.psa.api.dto.Dtos.CreateBizOptyRequest;
import io.hellcoding.psa.api.dto.Dtos.CreateProjectRequest;
import io.hellcoding.psa.api.dto.Dtos.CreateTaskRequest;
import io.hellcoding.psa.api.dto.Dtos.ProjectMemberResponse;
import io.hellcoding.psa.api.dto.Dtos.ProjectResponse;
import io.hellcoding.psa.api.dto.Dtos.ProjectTaskResponse;
import io.hellcoding.psa.service.ProjectService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/biz-opties")
    public ResponseEntity<BizOptyResponse> createBizOpty(@Valid @RequestBody CreateBizOptyRequest request) {
        BizOptyResponse response = toBizOpty(projectService.createBizOpty(request));
        return ResponseEntity.created(URI.create("/api/biz-opties/" + response.id())).body(response);
    }

    @GetMapping("/biz-opties")
    public List<BizOptyResponse> listBizOpties() {
        return projectService.listBizOpties().stream()
                .map(io.hellcoding.psa.api.dto.DtoMapper::toBizOpty)
                .toList();
    }

    @PostMapping("/projects")
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody CreateProjectRequest request) {
        ProjectResponse response = toProject(projectService.createProject(request));
        return ResponseEntity.created(URI.create("/api/projects/" + response.id())).body(response);
    }

    @GetMapping("/projects")
    public List<ProjectResponse> listProjects() {
        return projectService.listProjects().stream()
                .map(io.hellcoding.psa.api.dto.DtoMapper::toProject)
                .toList();
    }

    @GetMapping("/projects/{projectId}")
    public ProjectResponse getProject(@PathVariable Long projectId) {
        return toProject(projectService.getProject(projectId));
    }

    @PostMapping("/projects/{projectId}/members")
    public ResponseEntity<ProjectMemberResponse> addMember(
            @PathVariable Long projectId,
            @Valid @RequestBody AddProjectMemberRequest request
    ) {
        ProjectMemberResponse response = toProjectMember(projectService.addMember(projectId, request));
        return ResponseEntity.created(URI.create("/api/projects/" + projectId + "/members/" + response.id())).body(response);
    }

    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<ProjectTaskResponse> createTask(
            @PathVariable Long projectId,
            @Valid @RequestBody CreateTaskRequest request
    ) {
        ProjectTaskResponse response = toProjectTask(projectService.createTask(projectId, request));
        return ResponseEntity.created(URI.create("/api/projects/" + projectId + "/tasks/" + response.id())).body(response);
    }

    @GetMapping("/projects/{projectId}/tasks")
    public List<ProjectTaskResponse> listTasks(@PathVariable Long projectId) {
        return projectService.listTasks(projectId).stream()
                .map(io.hellcoding.psa.api.dto.DtoMapper::toProjectTask)
                .toList();
    }
}
