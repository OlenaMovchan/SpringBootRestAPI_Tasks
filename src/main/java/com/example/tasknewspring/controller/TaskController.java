package com.example.tasknewspring.controller;

import com.example.tasknewspring.aspect.LogExecutionTime;
import com.example.tasknewspring.dto.TaskDto;

import com.example.tasknewspring.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class TaskController {

    private TaskService taskService;

    @PostMapping()
    @Operation(summary = "Create task")
    @PreAuthorize("hasAuthority('ROLE_TEAM-LEAD')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The newly created task"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @LogExecutionTime
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskDto task, WebRequest request) {

        return new ResponseEntity<>(taskService.createTask(task, request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all tasks")
    @PreAuthorize("hasAnyAuthority('ROLE_DEVELOPER', 'ROLE_TEAM-LEAD')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page of the tasks"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @LogExecutionTime
    public ResponseEntity<?> getAllTasks(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

        return new ResponseEntity<>(taskService.getAllTasks(pageNo, pageSize), HttpStatus.OK);
    }

    @Operation(summary = "Get task by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_DEVELOPER', 'ROLE_TEAM-LEAD')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The task"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @LogExecutionTime
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @Operation(summary = "Get task by userId")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ROLE_DEVELOPER', 'ROLE_TEAM-LEAD')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page of the tasks by userId"),
    })
    @LogExecutionTime
    public ResponseEntity<?> getTasksByUserId(@PathVariable Integer userId,
                                              @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                              @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return new ResponseEntity<>(taskService.getTasksByUserId(userId, pageNo, pageSize), HttpStatus.OK);
    }

    @Operation(summary = "Update task by id")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_DEVELOPER', 'ROLE_TEAM-LEAD')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The updated task"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @LogExecutionTime
    public ResponseEntity<?> updateTaskById(@RequestBody TaskDto taskDto,
                                            @PathVariable("id") Long id) {
        TaskDto response = taskService.updateTask(taskDto, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Delete task by id")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_TEAM-LEAD')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @LogExecutionTime
    public ResponseEntity<?> deleteTask(@PathVariable("id") Long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>("Task delete", HttpStatus.OK);

    }

}



