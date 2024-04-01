package com.example.tasknewspring.service;

import com.example.tasknewspring.dto.TaskDto;
import com.example.tasknewspring.dto.TaskResponse;
import org.springframework.web.context.request.WebRequest;

import java.security.Principal;


public interface TaskService {
    TaskDto createTask(TaskDto task, WebRequest request);

    TaskResponse getAllTasks(int pageNo, int pageSize);

    TaskDto getTaskById(Long id);

    TaskResponse getTasksByUserId(Integer id, int pageNo, int pageSize);

    void deleteTask(Long id);

    TaskDto updateTask(TaskDto taskDto, Long id);
}
