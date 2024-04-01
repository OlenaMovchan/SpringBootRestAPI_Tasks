package com.example.tasknewspring.service.impl;

import com.example.tasknewspring.dto.TaskDto;
import com.example.tasknewspring.dto.TaskResponse;
import com.example.tasknewspring.entity.TaskEntity;
import com.example.tasknewspring.entity.TaskStatus;
import com.example.tasknewspring.entity.UserEntity;
import com.example.tasknewspring.exception.*;
import com.example.tasknewspring.exception.IllegalStateException;
import com.example.tasknewspring.repository.TaskRepository;
import com.example.tasknewspring.repository.UserEntityRepository;
import com.example.tasknewspring.service.TaskService;
import com.example.tasknewspring.util.LocaleMessage;
import com.example.tasknewspring.validators.TaskValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.WebRequest;

import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class TaskServiceImpl implements TaskService {
    private TaskRepository taskRepository;
    private ModelMapper mapper;
    private UserEntityRepository userEntityRepository;
    private LocaleMessage localeMessage;
    public Locale getLocaleFromHeader(WebRequest request) {

        String langHeader = request.getHeader("Accept-Language");
        return langHeader != null ? Locale.forLanguageTag(langHeader) : Locale.getDefault();

    }
    @Override
    public TaskDto createTask(TaskDto task, WebRequest request) {

        userEntityRepository.findById(task.getUserId()).orElseThrow(() ->
                new UserNotFoundException("User could not be found", ErrorCodes.USER_NOT_FOUND));
       Locale locale = getLocaleFromHeader(request);
        List<String> errors = TaskValidator.validateTask(task,locale, localeMessage);

        if (!errors.isEmpty()) {
            log.error("Task is not valid {}", task);
            throw new InvalidTaskException("Task is not valid", ErrorCodes.TASK_NOT_VALID, errors);
        }

        if (!task.getStatus().equals(TaskStatus.PLANNED)) {
            throw new IllegalStateException("The state of the task upon creation can only be initial - PLANNED",
                    ErrorCodes.INCORRECT_TASK_STATE);
        }

        TaskEntity newTask = taskRepository.save(toModel(task));

        return toDto(newTask);
    }

    @Override
    public TaskResponse getAllTasks(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<TaskEntity> tasks = taskRepository.findAll(pageable);
        return getTaskResponse(tasks);

    }

    @Override
    public TaskDto getTaskById(Long id) {

        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(() ->
                new TaskNotFoundException("Task could not be found", ErrorCodes.TASK_NOT_FOUND));

        return toDto(taskEntity);

    }

    @Override
    public TaskResponse getTasksByUserId(Integer id, int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<TaskEntity> tasks = taskRepository.findTaskEntitiesByUserId(id, pageable);

        return getTaskResponse(tasks);

    }

    @Override
    public TaskDto updateTask(TaskDto taskDto, Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            Optional<UserEntity> user = userEntityRepository.findByName(currentUserName);
            if (user.isPresent() && !taskDto.getUserId().equals(user.get().getId())) {
                throw new AccessDeniedException("You do not have permission to update this task.");
            }
        }

        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(() ->
                new TaskNotFoundException("Task could not be updated", ErrorCodes.TASK_NOT_FOUND));
        if (!taskEntity.getDescription().equals(taskDto.getDescription()) || !taskEntity.getDueDate().equals(taskDto.getDueDate())) {
            throw new IllegalArgumentException("You cannot change the due date and task description");
        }

        TaskStatus currentState = taskEntity.getStatus();
        TaskStatus nextState = taskDto.getStatus();
        if (!currentState.isValidTransition(nextState)) {
            throw new IllegalStateException("Invalid state transition from " + currentState + " to " + nextState,
                    ErrorCodes.INCORRECT_TASK_STATE);
        }

        taskDto.setId(id);

        return toDto(taskRepository.save(toModel(taskDto)));
    }

    @Override
    public void deleteTask(Long id) {

        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(() ->
                new TaskNotFoundException("Task could not be delete", ErrorCodes.TASK_NOT_FOUND));
        taskRepository.delete(taskEntity);

    }

    private TaskResponse getTaskResponse(Page<TaskEntity> tasks) {
        List<TaskEntity> listOfTasks = tasks.getContent();
        List<TaskDto> content = listOfTasks.stream().map(this::toDto).toList();
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setContent(content);
        taskResponse.setPageNo(tasks.getNumber());
        taskResponse.setPageSize(tasks.getSize());
        taskResponse.setTotalElements(tasks.getTotalElements());
        taskResponse.setTotalPages(tasks.getTotalPages());
        taskResponse.setLast(tasks.isLast());
        return taskResponse;
    }

    public TaskEntity toModel(TaskDto taskDto) {
        return mapper.map(taskDto, TaskEntity.class);
    }

    public TaskDto toDto(TaskEntity taskEntity) {
        return mapper.map(taskEntity, TaskDto.class);
    }
}
